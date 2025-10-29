import Vue from 'vue';
import Router from 'vue-router';
import store from '../store';


Vue.use(Router); {
	const originalPush = Router.prototype.push;
	Router.prototype.push = function push(location) {
		return originalPush.call(this, location).catch(err => err)
	}
}


export const defaultRouters = [
	{
			path: '/',
			redirect: '/login'
		},
		{
			path: '/login',
			component: () => import( /* webpackChunkName: "login" */ '../components/page/Login.vue'),
			meta: {
				title: '管理员/酒店登录'
			}
		},{
			path: '/Register',
			component: () => import( /* webpackChunkName: "login" */ '../components/page/Register.vue'),
			meta: {
				title: '客户注册'
			}
		},{
			path: '/HomeUser',
			component: () => import( /* webpackChunkName: "homeuser" */ '../components/common/HomeUser.vue'),
			meta: {
				title: '客户首页'
			},
			children:[
				{
					path: '/HomeUser/home',
					name: 'HomeUser-home',
					component: () => import( /* webpackChunkName: "homeuser" */ '../components/page/home.vue'),
				},
				{
					path: '/HomeUser/reserve',
					name: 'HomeUser-reserve',
					component: () => import( /* webpackChunkName: "homeuser" */ '../components/page/reserve.vue'),
				},
				{
					path: '/HomeUser/orders',
					name: 'HomeUser-orders',
					component: () => import( /* webpackChunkName: "homeuser" */ '../components/page/orders.vue'),
				},
				{
					path: '/HomeUser/history',
					name: 'HomeUser-history',
					component: () => import( /* webpackChunkName: "homeuser" */ '../components/page/history.vue'),
				},
				{
					path: '/HomeUser/profile',
					name: 'HomeUser-profile',
					component: () => import( /* webpackChunkName: "homeuser" */ '../components/page/profile.vue'),
				},
			]
		}

 ];


export default new Router({
	mode: 'history',
	base: '/',
	routes: defaultRouters

});
