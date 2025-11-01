/* "scripts": {
      "dev": "SET NODE_OPTIONS=--openssl-legacy-provider&&npm run serve",
      "serve": "SET NODE_OPTIONS=--openssl-legacy-provider&&vue-cli-service serve",
      "build": "SET NODE_OPTIONS=--openssl-legacy-provider&&vue-cli-service build"
    }, */

<template>
	<div id="app">
		<router-view></router-view>
	</div>
</template>
<style>
	@import "./assets/css/main.css";
	@import "./assets/css/color-dark.css";
	/*深色主题*/
	/*@import "./assets/css/theme-green/color-green.css";   浅绿色主题*/
</style>
<script>
	import {
		getDynamicMenu
	} from "./router/dynamicMenu.js";
	import {
		ajaxGet,
		ajaxPost
	} from "./api/index";
	export default {

		created() {
			if (sessionStorage.getItem("user")) {
				ajaxGet("/employee/router").then(res => {
				let roles=res;
				// let roles = JSON.parse(sessionStorage.getItem("roles"));
				// console.log(roles);
				if (roles && roles.length > 0) {
					this.$store.commit('setRoles', roles);
					// this.$store.commit('setFlag', true);
					getDynamicMenu();
				}else{//刷新时没有权限的处理
					this.$message.error({message:'您没有权限访问系统,请联系管理员',center: true});
					this.$router.push('/login');
				}
				})
				.catch(error => {
					console.error('获取路由权限失败:', error);
					// 使用try-catch避免在请求失败时页面卡死
					try {
						// 尝试使用本地缓存的角色信息
						let user = JSON.parse(sessionStorage.getItem("user"));
						if (user && user.roles && user.roles.length > 0) {
							this.$store.commit('setRoles', user.roles);
							getDynamicMenu();
						} else {
							this.$message.warning({message:'无法获取权限信息，已使用本地缓存',center: true});
							this.$router.push('/login');
						}
					} catch (e) {
						this.$router.push('/login');
					}
				})
			} else {
				let end = [{
						path: '/404',
						component: () => import( /* webpackChunkName: "404" */ './components/page/404.vue'),
						meta: {
							title: '404'
						}
					},
					{
						path: '*',
						redirect: '/404'
					}
				];
				this.$router.addRoutes(end);
			}
			//在页面刷新时将vuex里的信息保存到sessionStorage里
			// window.addEventListener("beforeunload", () => {
			// 	// sessionStorage.setItem('path', this.$route.path);
			// 	if (this.$store.state.roles.length > 0)
			// 		sessionStorage.setItem("roles", JSON.stringify(this.$store.state.roles));
			// });
		}
	}
</script>
