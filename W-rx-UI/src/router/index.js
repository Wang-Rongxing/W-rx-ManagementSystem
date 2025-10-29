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
		// {
		// 	path: '/ht',
		// 	redirect: '/ht/dashboard'
		// 	// component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
		// 	// meta: { title: '系统首页' }
			
		// },
		// {
		// 	path: '/ht/home',
		// 	component: () => import( /* webpackChunkName: "home" */ '../components/common/Home.vue'),
		// 	meta: {
		// 		title: '自述文件'
		// 	},
		// 	children: 
		// 	[
		// 	             {
		// 	                 path: '/ht/dashboard',
		// 	                 component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
		// 	                 meta: { title: '系统首页' }
		// 	             }
		// 	 ]
		// },
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
				title: '用户首页'
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
		// ,
		// {
		// 	path: '/404',
		// 	component: () => import( /* webpackChunkName: "404" */ '../components/page/404.vue'),
		// 	meta: {
		// 		title: '404'
		// 	}
		// }	,
		// {
		// 	path: '*',
		// 	redirect: '/404'
		// }
 ];

// store.state.items.forEach(item => {
	
// 	if (item.subs) {
		
// 		item.subs.forEach(sub => {
// 			let flag=false;
// 			store.state.roles.forEach(role=>{
// 				if(sub.meta.roles){
// 					for(let r1 of sub.meta.roles){
// 						if(r1===role)
// 						{flag=true;
// 						break;
// 						}
// 					}
// 				}else{
// 					flag=true;
// 				}
// 			})
// 			if(flag){
// 			childrens.push({
// 				path: sub.path,
// 				component: sub.component,
// 				meta: sub.meta
// 			})
// 		 }
// 		})
// 	} else {
// 		let flag=false;
// 		store.state.roles.forEach(role=>{
// 			if(item.meta.roles){
// 				for(let r1 of item.meta.roles){
// 					if(r1===role)
// 					{flag=true;
// 					break;
// 					}
// 				}
// 			}else{
// 				flag=true;
// 			}
// 		})
// 		if(flag){
// 		childrens.push({
// 			path: item.path,
// 			component: item.component,
// 			meta: item.meta
// 		})
// 		}
// 	}

// });
// childrens.push({
// 	path: '/ht/*',
// 	redirect: '/ht/404'
// });

// console.log(childrens);



export default new Router({
	mode: 'history',
	base: '/',
	routes: defaultRouters
	// [{
	// 		path: '/',
	// 		redirect: '/login'
	// 	},
	// 	{
	// 		path: '/ht',
	// 		redirect: '/ht/dashboard'
	// 		// component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
	// 		// meta: { title: '系统首页' }
			
	// 	},
	// 	{
	// 		path: '/ht/home',
	// 		component: () => import( /* webpackChunkName: "home" */ '../components/common/Home.vue'),
	// 		meta: {
	// 			title: '自述文件'
	// 		},
	// 		children: 
	// 		[
	// 		             {
	// 		                 path: '/ht/dashboard',
	// 		                 component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
	// 		                 meta: { title: '系统首页' }
	// 		             }
						 // ,
		// 	             {
		// 	                 path: '/ht/icon',
		// 	                 component: () => import(/* webpackChunkName: "icon" */ '../components/page/Icon.vue'),
		// 	                 meta: { title: '自定义图标' }
		// 	             },
		// 	             {
		// 	                 path: '/ht/table',
		// 	                 component: () => import(/* webpackChunkName: "table" */ '../components/page/BaseTable.vue'),
		// 	                 meta: { title: '基础表格' }
		// 	             },
		// 		{
		// 		    path: '/ht/test',
		// 		    component: () => import(/* webpackChunkName: "table" */ '../components/page/test.vue'),
		// 		    meta: { title: '单招考试' }
		// 		},
		// 		{
		// 		    path: '/ht/teacher',
		// 		    component: () => import(/* webpackChunkName: "table" */ '../components/page/teacher.vue'),
		// 		    meta: { title: '教师管理' }
		// 		},
		// 		{
		// 		    path: '/ht/student',
		// 		    component: () => import(/* webpackChunkName: "table" */ '../components/page/student.vue'),
		// 		    meta: { title: '学生管理' }
		// 		},
		// 		{
		// 		    path: '/ht/score',
		// 		    component: () => import(/* webpackChunkName: "table" */ '../components/page/score.vue'),
		// 		    meta: { title: '成绩管理' }
		// 		},
		// 	             {
		// 	                 path: '/ht/tabs',
		// 	                 component: () => import(/* webpackChunkName: "tabs" */ '../components/page/Tabs.vue'),
		// 	                 meta: { title: 'tab选项卡' }
		// 	             },
		// 	             {
		// 	                 path: '/ht/form',
		// 	                 component: () => import(/* webpackChunkName: "form" */ '../components/page/BaseForm.vue'),
		// 	                 meta: { title: '基本表单' }
		// 	             },
		// 	             {
		// 	                 // 富文本编辑器组件
		// 	                 path: '/ht/editor',
		// 	                 component: () => import(/* webpackChunkName: "editor" */ '../components/page/VueEditor.vue'),
		// 	                 meta: { title: '富文本编辑器' }
		// 	             },
		// 	             {
		// 	                 // markdown组件
		// 	                 path: '/ht/markdown',
		// 	                 component: () => import(/* webpackChunkName: "markdown" */ '../components/page/Markdown.vue'),
		// 	                 meta: { title: 'markdown编辑器' }
		// 	             },
		// 	             {
		// 	                 // 图片上传组件
		// 	                 path: '/ht/upload',
		// 	                 component: () => import(/* webpackChunkName: "upload" */ '../components/page/Upload.vue'),
		// 	                 meta: { title: '文件上传' }
		// 	             },
		// 	             {
		// 	                 // vue-schart组件
		// 	                 path: '/ht/charts',
		// 	                 component: () => import(/* webpackChunkName: "chart" */ '../components/page/BaseCharts.vue'),
		// 	                 meta: { title: 'schart图表' }
		// 	             },
		// 	             {
		// 	                 // 拖拽列表组件
		// 	                 path: '/ht/drag',
		// 	                 component: () => import(/* webpackChunkName: "drag" */ '../components/page/DragList.vue'),
		// 	                 meta: { title: '拖拽列表' }
		// 	             },
		// 	             {
		// 	                 // 拖拽Dialog组件
		// 	                 path: '/ht/dialog',
		// 	                 component: () => import(/* webpackChunkName: "dragdialog" */ '../components/page/DragDialog.vue'),
		// 	                 meta: { title: '拖拽弹框' }
		// 	             },
		// 	             {
		// 	                 // 国际化组件
		// 	                 path: '/ht/i18n',
		// 	                 component: () => import(/* webpackChunkName: "i18n" */ '../components/page/I18n.vue'),
		// 	                 meta: { title: '国际化' }
		// 	             },
		// 	             {
		// 	                 // 权限页面
		// 	                 path: '/ht/permission',
		// 	                 component: () => import(/* webpackChunkName: "permission" */ '../components/page/Permission.vue'),
		// 	                 meta: { title: '权限测试', permission: true }
		// 	             },
		// 	             {
		// 	                 path: '/ht/404',
		// 	                 component: () => import(/* webpackChunkName: "404" */ '../components/page/ht404.vue'),
		// 	                 meta: { title: '404' }
		// 	             },
		// 	             {
		// 	                 path: '/ht/403',
		// 	                 component: () => import(/* webpackChunkName: "403" */ '../components/page/403.vue'),
		// 	                 meta: { title: '403' }
		// 	             },
		// 	             {
		// 	                 path: '/ht/donate',
		// 	                 component: () => import(/* webpackChunkName: "donate" */ '../components/page/Donate.vue'),
		// 	                 meta: { title: '支持作者' }
		// 	             },
		// 		{
		// 		    path: '/ht/*',
		// 		    redirect: '/ht/404'
		// 		}
		// 	 ]
		// },
		// {
		// 	path: '/login',
		// 	component: () => import( /* webpackChunkName: "login" */ '../components/page/Login.vue'),
		// 	meta: {
		// 		title: '登录'
		// 	}
		// }
		// ,
		// {
		// 	path: '*',
		// 	redirect: '/login'
		// }
		// {
		// 	path: '/404',
		// 	component: () => import( /* webpackChunkName: "404" */ '../components/page/404.vue'),
		// 	meta: {
		// 		title: '404'
		// 	}
		// }	
	// ]
});
