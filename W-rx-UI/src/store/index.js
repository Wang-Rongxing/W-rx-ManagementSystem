import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex)

const state = {
	draftsObj: {},
	name: 'zhangsan',
	flag: true,
	roles: [],
	items: [
		{
			path: '/ht/dashboard',
			component: () => import( /* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
			meta: {
				title: '系统首页'
			},
			icon: 'el-icon-s-home',
			index: 'dashboard',
			title: '系统首页'
		},
		{
			path: '/ht/employee',
			component: () => import( /* webpackChunkName: "table" */ '../components/page/employee.vue'),
			meta: {
				title: '酒店员工',
				roles: ['admin']
			},
			icon: 'el-icon-s-custom',
			index: 'employee',
			title: '酒店员工'
		},
		{
			path: '/ht/permission',
			component: () => import( /* webpackChunkName: "table" */'../components/page/permission.vue'),
			meta: {
				title: '权限管理',
				roles: ['admin']
			},
			icon: 'el-icon-s-check',
			index: 'permission',
			title: '权限管理'
		},
		{
			path: '/ht/customer',
			component: () => import( /* webpackChunkName: "table" */'../components/page/student.vue'),
			meta: {
				title: '客户管理',
				roles: ['manager']
			},
			icon: 'el-icon-user',
			index: 'customer',
			title: '客户管理'
		},
		{
			path: '/ht/order',
			component: () => import( /* webpackChunkName: "table" */'../components/page/Order.vue'),
			meta: {
				title: '订单管理',
				roles: ['manager']
			},
			icon: 'el-icon-document',
			index: 'order',
			title: '订单管理'
		},
		{
			path: '/ht/room',
			component: () => import( /* webpackChunkName: "table" */'../components/page/Room.vue'),
			meta: {
				title: '客房管理',
				roles: ['roomattendant']
			},
			icon: 'el-icon-s-home',
			index: 'room',
			title: '客房管理'
		},
		{
			path: '/ht/checkin',
			component: () => import( /* webpackChunkName: "table" */'../components/page/Checkin.vue'),
			meta: {
				title: '入住登记',
				roles: ['manager']
			},
			icon: 'el-icon-s-order',
			index: 'checkin',
			title: '入住登记'
		},
		{
			path: '/ht/404',
			component: () => import( /* webpackChunkName: "404" */ '../components/page/ht404.vue'),
			meta: {
				title: '404'
			}
		},
		{
			path: '/ht/403',
			component: () => import( /* webpackChunkName: "403" */ '../components/page/403.vue'),
			meta: {
				title: '403'
			}
		},

	]

}

const getters = {
	getDraftsObj: state => state.draftsObj

}

const mutations = {
	// setDraftsObj(state, obj) {
	//   state.draftsObj = { ...obj };
	// }
	setFlag(state, obj) {
		state.flag = obj;
	},
	jian(state) {
		state.flag--;
	},
	setRoles(state, obj) {
		state.roles = obj;
	}
}

const actions = {
	setRoles({
		commit
	}, obj) {
		commit('setRoles', obj);
	}

}

export default new Vuex.Store({
	state,
	getters,
	mutations,
	actions
})
