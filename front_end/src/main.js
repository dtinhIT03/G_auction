import { createApp } from 'vue'
import router from './router/index.js'
import App from './App.vue'
import components from './components/index.js';
import store from './stores/store.js';

import 'ant-design-vue/dist/reset.css';
import './assets/css/tailwind.scss';

const app = createApp(App);

app.use(router);
app.use(store);
app.mount('#app');

Object.values(components).forEach(component => {
  app.use(component);
});
