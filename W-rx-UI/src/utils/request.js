import axios from 'axios';
import router from '../router';
import ElementUI from 'element-ui';
import baseUrl from './baseUrl';

const service = axios.create({
	// process.env.NODE_ENV === 'development' 来判断是否开发环境
	baseURL: baseUrl,
	// headers: {
	// 	'Content-Type': "application/json; charset=utf-8"
	// },
	timeout: 10000
});

service.interceptors.request.use(
	config => {
		let user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : null;
		if (user) {
			config.headers['token'] = user.token;
		}
		return config;
	},
	error => {
		console.log(error);
		return Promise.reject();
	}
);

service.interceptors.response.use(
	response => {
		if (response.headers['token']) {
			if (sessionStorage.getItem("user")) {
				let user = JSON.parse(sessionStorage.getItem("user"));
				user.token = response.headers['token'];
				sessionStorage.setItem("user", JSON.stringify(user));
			}
		}
		if (response.status === 200) {
			return response.data;
		} else {
			Promise.reject();
		}
	},
	error => {
		// 401没权限
		console.log('请求错误:', error);
		
		// 判断是否是页面初次加载或刷新时发生的错误
		const isPageLoadOrRefresh = !router.history.current.matched.length;
		
		if (error.response) { //如果服务器有错误回应
			if (error.response.status === 401 ) { //如果没有权限
				sessionStorage.removeItem("user");
				//输出来自服务器的异常信息
				ElementUI.Message.error({
					message: error.response.data.msg ? error.response.data.msg : "权限不足,或登录超时，请重新登录",
					duration: 6000,
					center: true
				});
				router.push('/login'); //要求重新登录
			} else {
				// 对于页面加载或刷新时的非401错误，不显示错误信息，让App.vue中的错误处理逻辑来处理
				if (!isPageLoadOrRefresh) {
					//输出来自服务器的异常信息
					ElementUI.Message.error({
						message: error.response.data.msg ? error.response.data.msg : "服务器没有提供此服务",
						duration: 6000,
						center: true
					});
				}
			}
		} else if (!isPageLoadOrRefresh) { //没有连接上服务器的错误提示，且不是页面加载或刷新时的错误
			// 只在用户操作过程中的请求失败时显示错误信息
			ElementUI.Message.error({
				message: "网络连接失败，请检查您的网络设置",
				duration: 4000,
				center: true
			});
		}
		
		return Promise.reject(error); // 传递错误对象，方便上层组件处理
	}
);

// 导出
export const axiosExport = (axiosConfig) => {
    const service = axios.create({
        baseURL: baseUrl, // 设置统一的请求前缀
        timeout: 10000, // 设置统一的超时时长
    });
    // 添加请求拦截器
    service.interceptors.request.use(function (config) {
        // 在发送请求之前做些什么
        // let token = localStorage.getItem('token')
		let user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : null;
        if(user){
            // 添加请求头
           // config.headers.Authorization = user.token;
			 config.headers['token'] = user.token;
        }
        return config;
    }, function (error) {
        // 对请求错误做些什么
        return Promise.reject(error);
    });
    // 添加响应拦截器
    service.interceptors.response.use(response => {
        // 将文件流转成blob形式
        // const blob = new Blob([response.data], {type: 'application/vnd.ms-excel'});
		const blob = new Blob([response.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
		//decodeURIComponent解码
		 const fileName = decodeURIComponent(response.headers['content-disposition']).split('filename=')[1];
		//const fileName = response.headers['content-disposition'].split('filename=')[1];
        if(window.navigator.msSaveOrOpenBlob) {// 兼容IE10
            navigator.msSaveBlob(blob, fileName);
        } else {
            // 创建一个超链接，将文件流赋进去，然后实现这个超链接的单击事件
            const elink = document.createElement('a');
			
            elink.download = fileName;
            elink.style.display = 'none';
            elink.href = URL.createObjectURL(blob);
            document.body.appendChild(elink);
            elink.click();
            URL.revokeObjectURL(elink.href); // 释放URL 对象
            document.body.removeChild(elink);
        }
    }, (error) => {
        // 对响应错误做点什么
        if(error){
            message.error(error.response.data?error.response.data.message:'系统错误')
        }
        return Promise.reject(error.response.data);
    });
    return service({...axiosConfig, responseType: 'blob'})
}


export default service;
