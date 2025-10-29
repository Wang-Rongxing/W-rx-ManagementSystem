import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import store from './store';
// import Plugin from 'v-fit-columns';
import AFTableColumn from 'af-table-column';
import VueI18n from 'vue-i18n';
import {
	messages
} from './components/common/i18n';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import './assets/css/theme-green/index.css'; // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import 'babel-polyfill';
//import axios from 'axios';
//Vue.prototype.$axios = axios;
//axios.defaults.baseURL = "http://localhost:8080/project";

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

import {
	fetchData,
	ajaxPost,
	ajaxGet,
	ajaxDelete
} from './api/index';
import {
		getDynamicMenu
	} from "./router/dynamicMenu.js";
import {
	Store
} from 'vuex';



// const childrens = [];

// const addMenu = [{
// 		path: '/ht',
// 		redirect: '/ht/dashboard'
// 		// component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
// 		// meta: { title: '系统首页' }
// 	},
// 	{
// 		path: '/ht/home',
// 		component: () => import( /* webpackChunkName: "home" */ './components/common/Home.vue'),
// 		meta: {
// 			title: '自述文件'
// 		},
// 		children: childrens
// 	}
// ];

// function getMenu() {

// 	store.state.items.forEach(item => {

// 		if (item.subs) {
// 			item.subs.forEach(sub => {
// 				let flag = false;
// 				store.state.roles.forEach(role => {
// 					if (sub.meta.roles) {
// 						for (let r1 of sub.meta.roles) {
// 							if (r1 === role) {
// 								flag = true;
// 								continue;
// 							}
// 						}
// 					} else {
// 						flag = true;
// 					}
// 				})
// 				if (flag) {
// 					childrens.push({
// 						path: sub.path,
// 						component: sub.component,
// 						meta: sub.meta
// 					})
// 				}
// 			})
// 		} else {
// 			let flag = false;
// 			store.state.roles.forEach(role => {
// 				if (item.meta.roles) {
// 					for (let r1 of item.meta.roles) {
// 						if (r1 === role) {
// 							flag = true;
// 							continue;
// 						}
// 					}
// 				} else {
// 					flag = true;
// 				}
// 			})
// 			if (flag) {
// 				childrens.push({
// 					path: item.path,
// 					component: item.component,
// 					meta: item.meta
// 				})
// 			}
// 		}

// 	});
// 	childrens.push({
// 		path: '/ht/*',
// 		redirect: '/ht/404'
// 	});
// }


//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
			//document.title = `${to.meta.title} | 酒店管理系统`;
			document.title = `飛鱼酒店`;
               
			

				//console.log(router.getRoutes());
				// const role = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token: null;
				// if (!role && to.path !== '/login') {
				// 	// if (!role && to.path == '/dashboard') {
				// 	// ElementUI.Message.warning({
				// 	// 	message: '您没有访问权限，请登录系统',
				// 	// 	duration: 6000,
				// 	// 	center: true
				// 	// });
				// 	next('/login');
				// } else 
				// 		if (to.meta.permission) {
				// 			// 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
				// 			role === 'admin' ? next() : next('/403');
				// 		} else {
				// 			// 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
				// 			if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
				// 				Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
				// 					confirmButtonText: '确定'
				// 				});
				// 			} else {
				// 				console.log('aaaaaaa');
				// 				next();
				// 			}
				// 		}

				// 	})
				// } else {
				 	// next();
				// }
				// next({ path: to.path });
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
