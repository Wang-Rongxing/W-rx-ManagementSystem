import Vue from 'vue';
import Router from 'vue-router';
import router from './';
import {defaultRouters} from './';
import store from '../store';




function getMenu(){
    let childrens = [];
    let sItem=store.state.items;
	sItem.forEach(item => {
           
		if (item.subs&&item.subs.length>0) {
			item.subs.forEach(sub => {
				let flagsub = false;
				store.state.roles.forEach(role => {
					if (sub.meta.roles) {
						for (let r1 of sub.meta.roles) {
							if (r1 === role) {
								flagsub = true;
								break;
							}
						}
					} else {
						flagsub = true;
					}
				})
				if (flagsub) {
					childrens.push({
						path: sub.path,
						component: sub.component,
						meta: sub.meta
					})
					
				}
			})
		} else {
			let flag = false;
			store.state.roles.forEach(role => {
				if (item.meta.roles) {
					for (let r1 of item.meta.roles) {
						if (r1 === role) {
							flag = true;
							break;
						}
					}
				} else {
					flag = true;
				}
			})
			if (flag) {
				childrens.push({
					path: item.path,
					component: item.component,
					meta: item.meta
				})
			}
		}
	});
	childrens.push({
		path: '/ht/*',
		redirect: '/ht/404'
	});
	let addMenu = [{
			path: '/ht',
			redirect: '/ht/dashboard'
			// component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
			// meta: { title: '系统首页' }
		},
		{
			path: '/ht/home',
			component: () => import( /* webpackChunkName: "home" */ '../components/common/Home.vue'),
			meta: {
				title: '自述文件'
			},
			children: childrens
		}
		,
		{
			path: '*',
			redirect: '/login'
		}
	];
	return addMenu;
}

export  const getDynamicMenu=()=>{
	  // && store.state.flag === true
	 //if (store.state.roles.length > 0) {
		// console.log(store.state.roles);
		resetRouter (router);
		let m= getMenu();
		//console.log(m);
		router.addRoutes(m);
		//store.commit('setFlag', false);
	//}
	 	
	
}

export function resetRouter (router) {
	
  const createRouter = () =>
    new Router({
      mode: 'history',
      routes: defaultRouters
    })
  // 用初始化的matcher替换当前router的matcher
  router.matcher = createRouter().matcher;
  // router.matcher=new Router({
  //     mode: 'history',
  //     routes: defaultRouters
  //   }).matcher;
 //console.log(router.getRoutes());
}

