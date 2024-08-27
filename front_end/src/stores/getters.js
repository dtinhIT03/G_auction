export default {
   getLoginState(state){
      return state.isLogin;
   },
   getUser(state){
      return state.user;
   },
   getIsAdmin(state){
      return state.isAdmin;
   },
   getLoading(state){
      return state.isLoading;
   },
   // getEmail(state) {
   //    return state.email;
   // },
   getImages(state) {
      return state.images;
   },
   getProducts(state) {
      return state.products;
   },
   getAuction(state){
      return state.auction;
   },
   getFilterProducts(state) {
      return state.filterProducts;
   },
   getProductDetail(state) {
      return state.productDetail;
   },
   getSessions(state) {
      return state.sessions;
   },
   getFilterAuctions(state){
      return state.filterAuctions;
   }
} 