import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import store from './store';
import AFTableColumn from 'af-table-column';
import VueI18n from 'vue-i18n';
import {
	messages
} from './components/common/i18n';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
import './assets/css/icon.css';
import './components/common/directives';
import 'babel-polyfill';


Vue.config.productionTip = false;
Vue.use(VueI18n);
Vue.use(ElementUI, { size: 'mini', zIndex: 3000 });
// Vue.use(Plugin);
Vue.use(AFTableColumn);
const i18n = new VueI18n({
	locale: 'zh',
	messages
});
Vue.use(router);

//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
			//document.title = `${to.meta.title} | 酒店管理系统`;
			document.title = `酒店`;
				console.log(to.path);
				console.log(router.getRoutes());
				
				 next();
			});
		new Vue({
			i18n,
			store,
			router,
			render: h => h(App)
		}).$mount('#app');
