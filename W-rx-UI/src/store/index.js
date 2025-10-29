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
		// {
		// 	path: '/ht/table',
		// 	component: () => import( /* webpackChunkName: "table" */ '../components/page/BaseTable.vue'),
		// 	meta: {
		// 		title: '基础表格',
		// 		roles: ['ROLE_edu_admin', 'ROLE_sys_admin']
		// 	},
		// 	icon: 'el-icon-lx-cascades',
		// 	index: 'table',
		// 	title: '基础表格'
		// },
		{
			path: '/ht/test',
			component: () => import( /* webpackChunkName: "table" */ '../components/page/test.vue'),
			meta: {
				title: '单招考试',
				roles: ['ROLE_teacher','ROLE_edu_admin']
			},
			icon: 'el-icon-edit-outline',
			index: 'test',
			title: '单招考试'
		},
		{
			icon: 'el-icon-document',
			index: '1',
			title: '考试管理',
			meta: {
				roles: ['ROLE_sys_admin','ROLE_edu_admin']
			},
			subs: [{
					path: '/ht/teacher',
					component: () => import( /* webpackChunkName: "table" */
						'../components/page/teacher.vue'),
					meta: {
						title: '教师管理',
						roles: ['ROLE_sys_admin','ROLE_edu_admin']
					},
					index: 'teacher',
					title: '教师管理'
				},

				{
					path: '/ht/student',
					component: () => import( /* webpackChunkName: "table" */
						'../components/page/student.vue'),
					meta: {
						title: '学生管理',
						roles: ['ROLE_sys_admin','ROLE_edu_admin']
					},
					index: 'student',
					title: '学生管理'
				},
				{
					path: '/ht/score',
					component: () => import( /* webpackChunkName: "table" */
						'../components/page/score.vue'),
					meta: {
						title: '成绩管理',
						roles: ['ROLE_sys_admin','ROLE_edu_admin']
					},
					index: 'score',
					title: '成绩管理'
				}
			]
		},
		{
			icon: 'el-icon-setting',
			index: '2',
			title: '系统管理',
			meta: {
				roles: ['ROLE_sys_admin']
			},
			subs: [{
					path: '/ht/role',
					component: () => import( /* webpackChunkName: "table" */
						'../components/page/role.vue'),
					meta: {
						title: '角色管理',
						roles: ['ROLE_sys_admin']
					},
					index: 'role',
					title: '角色管理'
				},
		
				{
					path: '/ht/permission',
					component: () => import( /* webpackChunkName: "table" */
						'../components/page/permission.vue'),
					meta: {
						title: '权限管理',
						roles: ['ROLE_sys_admin']
					},
					index: 'permission',
					title: '权限管理'
				}
			]
		},
		// {
		// 	path: '/ht/tabs',
		// 	component: () => import( /* webpackChunkName: "tabs" */ '../components/page/Tabs.vue'),
		// 	meta: {
		// 		title: 'tab选项卡',
		// 		roles: ['ROLE_edu_admin']
		// 	},
		// 	icon: 'el-icon-lx-copy',
		// 	index: 'tabs',
		// 	title: 'tab选项卡'
		// },
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
		// {
		// 		    path: '/ht/*',
		// 		    redirect: '/ht/404'
		// 		}

	]
	//   [
	//     {
	//         icon: 'el-icon-lx-home',
	//         index: 'dashboard',
	//         title: '系统首页'
	//     },
	//     {
	//         icon: 'el-icon-lx-cascades',
	//         index: 'table',
	//         title: '基础表格'
	//     },
	// 	{
	// 	    icon: 'el-icon-lx-cascades',
	// 	    index: 'test',
	// 	    title: '单招考试'
	// 	},
	// 	{
	// 	    icon: 'el-icon-lx-calendar',
	// 	   index: '1',
	// 	    title: '考试管理',
	// 	    subs: [
	// 	        {
	// 	            index: 'teacher',
	// 	            title: '教师管理'
	// 	        },

	// 	        {
	// 	            index: 'student',
	// 	            title: '学生管理'
	// 	        } ,
	// 			{
	// 			    index: 'score',
	// 			    title: '成绩管理'
	// 			} 
	// 	    ]
	// 	},
	//     {
	//         icon: 'el-icon-lx-copy',
	//         index: 'tabs',
	//         title: 'tab选项卡'
	//     },
	//     {
	//         icon: 'el-icon-lx-calendar',
	//         index: '5',
	//         title: '表单相关',
	//         subs: [
	//             {
	//                 index: 'form',
	//                 title: '基本表单'
	//             },
	//             {
	//                 index: '5-2',
	//                 title: '三级菜单',
	//                 subs: [
	//                     {
	//                         index: 'editor',
	//                         title: '富文本编辑器'
	//                     },
	//                     {
	//                         index: 'markdown',
	//                         title: 'markdown编辑器'
	//                     }
	//                 ]
	//             },
	//             {
	//                 index: 'upload',
	//                 title: '文件上传'
	//             }
	//         ]
	//     },
	//     {
	//         icon: 'el-icon-lx-emoji',
	//         index: 'icon',
	//         title: '自定义图标'
	//     },
	//     {
	//         icon: 'el-icon-pie-chart',
	//         index: 'charts',
	//         title: 'schart图表'
	//     },
	//     {
	//         icon: 'el-icon-rank',
	//         index: '6',
	//         title: '拖拽组件',
	//         subs: [
	//             {
	//                 index: 'drag',
	//                 title: '拖拽列表'
	//             },
	//             {
	//                 index: 'dialog',
	//                 title: '拖拽弹框'
	//             }
	//         ]
	//     },
	//     {
	//         icon: 'el-icon-lx-global',
	//         index: 'i18n',
	//         title: '国际化功能'
	//     },
	//     {
	//         icon: 'el-icon-lx-warn',
	//         index: '7',
	//         title: '错误处理',
	//         subs: [
	//             {
	//                 index: 'permission',
	//                 title: '权限测试'
	//             },
	//             {
	//                 index: '404',
	//                 title: '404页面'
	//             }
	//         ]
	//     },
	//     {
	//         icon: 'el-icon-lx-redpacket_fill',
	//         index: '/donate',
	//         title: '支持作者'
	//     }
	// ] 
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
