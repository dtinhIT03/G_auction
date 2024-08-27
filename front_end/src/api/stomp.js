import { Client } from '@stomp/stompjs';
import { v4 as uuidv4 } from 'uuid';
import { jwtDecode } from 'jwt-decode';

/// convention for global function declaration:
/// - stateful functions (functions that refer to global variables) are declared with function expressions
/// - stateless functions are declared with arrow functions

const WS_URL = import.meta.env.VITE_WS_URL;

/**
 * @type {StompSession | null}
 */
let stompSession = null;

const controlCallbacks = {};
const connectCallbacks = [];
const disconnectCallbacks = [];

function setOnConnect(callback) { 
    if (typeof callback !== 'function') {
        throw new Error('Callback is required');
    }
    connectCallbacks.push(callback);
};

function setOnDisconnect(callback) {
    if (typeof callback !== 'function') {
        throw new Error('Callback is required');
    }
    disconnectCallbacks.push(callback);
};

async function setup() {
    const token = localStorage.getItem('token');
    if (!token) {
        throw new Error('No token found');
    }
    stompSession = new StompSession({
      tokenProvider: () => localStorage.getItem('token'),
      connectCallbacks,
      disconnectCallbacks,
      controlCallbacks
    });
    await stompSession.init();
};

async function teardown() {
    stompSession.deactivate();
    stompSession = null;
};

function onControlMessage(type, callback) {
    controlCallbacks[type] = callback;
};

/// if callback is not provided, the function should return a promise
function subscribe(destination, onMessage, callback, errorCallback) {
    const currentSession = stompSession;
    const toWait = currentSession.reconnect?.promise ?? Promise.resolve();
    const promise = toWait.then(() => {
        return currentSession.subscribe(destination, onMessage, callback, errorCallback)
    });
    if (callback === undefined) {
        return promise;
    }
}

// return if the destination was subscribed (and thus unsubscribed)
function unsubscribe(destination) {
    stompSession.unsubscribe(destination);
}

/// if callback is not provided, the function should return a promise
function send(destination, payload, callback, errorCallback) {
    const currentSession = stompSession;
    const toWait = currentSession.reconnect?.promise ?? Promise.resolve();
    const promise = toWait.then(() => {
        return currentSession.send(destination, payload, callback, errorCallback)
    });
    if (callback === undefined) {
        return promise;
    }
}

export default {
    setOnConnect,
    setOnDisconnect,
    setup,
    teardown,
    onControlMessage,
    subscribe,
    unsubscribe,
    send
};


class StompSession {
    /**
     * @typedef {Object} Config
     * @property {Function} tokenProvider
     * @property {Array} connectCallbacks
     * @property {Array} disconnectCallbacks
     * @property {Object} controlCallbacks
     */

    /**
     * @type {Config}
     */
    #config;
    #isActive;
    #userId;
    #connection;
    #receiptCallbackRegistry;
    
    /**
     * @typedef {Object} Reconnect
     * @property {Promise<Void>} promise
     * @property {Function} abort 
     */

    /**
     * @type {Reconnect?}
     */
    reconnect;

    static async create(config) {
        const instance = new StompSession(config);
        await instance.init();
        return instance;
    }

    deactivate() {
        this.#isActive = false;
        this.reconnect?.abort();
        this.#teardownConnection();
    }

    constructor(config) {
        this.#config = config;
    }

    async init() {
        const token = this.#config.tokenProvider();
        if (!token) {
            throw new Error('No token found');
        }
        this.#isActive = true;
        this.#userId = jwtDecode(token).id;  
        this.#receiptCallbackRegistry = {};
        this.#attemptConnect();
    }

    get isActive() {
        return this.#isActive;
    }

    get isConnected() {
        return !!this.#connection?.client.connected;
    }

    
    /// if callback is not provided, the function should return a promise
    subscribe(destination, onMessage, callback, errorCallback) {
        if (!this.isConnected) {
            throw new Error('Not connected');
        }
        const { client, subscriptionRegistry } = this.#connection;

        const headers = this.#generateHeaders();
        const subscription = client.subscribe(destination, onMessage, headers);
        
        const messageId = headers['receipt'];
        if (callback !== undefined) {
            this.#registerReceiptCallback(messageId,
                (message) => {
                    subscriptionRegistry[messageId] = subscription;
                    callback?.(message);
                }, 
                errorCallback);
        } else {
            return new Promise((resolve, reject) => {
                this.#registerReceiptCallback(messageId, 
                    (message) => {
                        subscriptionRegistry[messageId] = subscription;
                        resolve(message);
                    },    
                    reject);
            });
        }
    }

    // return if the destination was subscribed (and thus unsubscribed)
    unsubscribe(destination) {
        if (!this.isConnected) {
            throw new Error('Not connected');
        }
        const { subscriptionRegistry } = this.#connection;
        const subscription = subscriptionRegistry[destination];
        if (subscription) {
            subscription.unsubscribe();
            delete subscriptionRegistry[destination];
        }
        return !!subscription;
    }

    /// if callback is not provided, the function should return a promise
    send(destination, payload, callback, errorCallback) {
        if (!this.isConnected) {
            throw new Error('Not connected');
        }

        const headers = this.#generateHeaders();
        this.#connection.client.publish({ 
            destination, 
            body: JSON.stringify(payload), 
            headers 
        });
        
        const messageId = headers['receipt'];
        if (callback !== undefined) {
            this.#registerReceiptCallback(messageId, callback, errorCallback);
        } else {
            return new Promise((resolve, reject) => {
                this.#registerReceiptCallback(messageId, resolve, reject);
            });
        }
    }



    async #attemptConnect() {
        let resolve, reject;
        this.reconnect = {
            promise: new Promise((res, rej) => {
                resolve = res;
                reject = rej;
            }),
            abort: () => reject(new Error('Connection aborted'))
        };
        while (this.isActive) {
            try {
                await this.#setupConnection();
                if (!this.isActive) {
                    return;
                }
                resolve();
                this.reconnect = undefined;
                return;
            } catch (error) {
                console.error('Failed to connect', error);
                await new Promise((res) => setTimeout(res, 5000));
            }
        }
    }

    async #setupConnection() {
        if (!this.isActive) {
            return;
        }
        this.#connection = await createConnection(this.#config.tokenProvider());
        if (!this.isActive) {
            return;
        }
        this.#setupErrorHandler();
        await this.#setupSubscriptions();
        if (!this.isActive) {
            return;
        }
        this.#callConnectCallbacks();
    }

    #teardownConnection() {
        this.#connection?.client?.deactivate();
        this.#connection = null;
    }

    
    #generateHeaders() {
        return {
            "receipt": uuidv4(),
            "Authorization": `Bearer ${this.#config.tokenProvider()}`
        }
    }

    
    #setupErrorHandler() {
        if (!this.isConnected) {
            throw new Error('Not connected');
        }

        const client = this.#connection.client;
        client.onStompError = (frame) => {
            console.error('STOMP error', frame);
            this.#onDisconnect();
        };
        client.onWebSocketClose = (event) => {
            console.log('WebSocket closed', event);
            this.#onDisconnect();
        };
        client.onWebSocketError = (error) => {
            console.error('WebSocket error', error);
            this.#onDisconnect();
        }
        client.onDisconnect = () => {
            console.log('Disconnected from WebSocket');
            this.#onDisconnect();
        };
    }

    async #setupSubscriptions() {
        if (!this.isConnected) {
            throw new Error('Not connected');
        }
    
        // this is a workaround for spring STOMP's lack of support for RECEIPT frame
        // this call must be placed before any other subscribe calls,
        // as this is what enables receiving responses to other requests,
        // including `subscribe` itself
        try {
            await this.subscribe(
                `/user/${this.#userId}/queue/receipts`, 
                (message) => {
                    this.#handleReceipt(message);
                }
            );
            console.log('subscribed to receipts');
        } catch (e) {
            console.log('Failed to subscribe to receipts', e);
            throw e;
        }
        
        try {
            await this.subscribe(
                `/user/${this.#userId}/queue/control`, 
                (message) => {
                    this.#handleControlMessage(message);
                }
            );
            console.log('subscribed to control');
        } catch (e) {
            console.log('Failed to subscribe to control', e);
            throw e;
        }
    }
  
    
    #registerReceiptCallback(messageId, callback, errorCallback) {
        this.#receiptCallbackRegistry[messageId] = [callback, errorCallback];
        if (errorCallback) {
            setTimeout(() => {
                if (this.isActive && this.#receiptCallbackRegistry[messageId]) {
                    delete this.#receiptCallbackRegistry[messageId];
                    errorCallback(new Error('Request timed out'));
                }
            }, 5000);
        }
    }

    #onDisconnect() {
        this.#teardownConnection();
        this.#callDisconnectCallbacks();
        this.#attemptConnect();
    }

  
    #handleReceipt(message) {
        const messageId = message.headers['receipt-id'];
        const response = JSON.parse(message.body);
        const callbackEntry = this.#receiptCallbackRegistry[messageId];
        delete this.#receiptCallbackRegistry[messageId];  
        if (!callbackEntry) {
            return;
        }
      
        const { success } = response;
        const [callback, errorCallback] = callbackEntry;
        (success ? callback : errorCallback)?.(response);
    }

    #handleControlMessage(message) {
        const body = JSON.parse(message.body);
        const type = body['type'];
        const payload = body['data'];
        const callback = this.#config.controlCallbacks[type];
        if (callback !== undefined) {
            callback?.(payload);
        } else {
            console.log('Unhandled control message:', type, payload);
        }
    }

    
    #callConnectCallbacks() {
        this.#config.connectCallbacks.forEach(callback => {
            try {
                callback();
            } catch (error) {
                console.error('Error in connect callback', error);
            }
        });
    }

    #callDisconnectCallbacks() {
        this.#config.disconnectCallbacks.forEach(callback => {
            try {
                callback();
            } catch (error) {
                console.error('Error in disconnect callback', error);
            }
        });
    }
}


const createConnection = async (token) => {
    const client = await createStompClient(token);
    const subscriptionRegistry = {};
    return { client, subscriptionRegistry };
}


const createWebSocket = (token) => {
    console.log(`connecting to ${WS_URL} with token ${token}`);
    const socket = new WebSocket(WS_URL, [
      'Authorization-Bearer', token
    ]);
    socket.onopen = () => {
        console.log('WebSocket connected');
    };
    socket.onerror = (ev) => {
        console.error('WebSocket error', ev);
    };
    socket.onclose = (ev) => {
        console.log('WebSocket closed', ev);
    };
    return socket;
}

const createStompClient = (token) => {
    console.log('Creating STOMP client');
    return new Promise((resolve, reject) => {
        let initialized = false;
        const client = new Client({
            webSocketFactory: () => createWebSocket(token),
            connectHeaders: {
                login: "",
                passcode: "",
                Authorization: `Bearer ${token}`
            },
            reconnectDelay: 0,
            onConnect: (frame) => {
                console.log('Connected: ' + frame);
                initialized = true;
                resolve(client);
            },
            onWebSocketError: (error) => {
                console.error('WebSocket error', error);
                if (!initialized) {
                    reject(error);
                }
            },
            onWebSocketClose: (event) => {
                console.log('WebSocket closed', event);
                if (!initialized) {
                    reject(event);
                }
            },
            onStompError: (frame) => {
                console.error('STOMP error', frame);
                if (!initialized) {
                    reject(frame);
                }
            },
            onDisconnect: () => {
                console.log('Disconnected from WebSocket');
            }
        });
        client.debug = console.log;
        console.log('Activating STOMP client');
        client.activate();
    });
}
