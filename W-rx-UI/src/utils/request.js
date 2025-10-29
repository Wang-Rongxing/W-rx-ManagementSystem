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
		console.log(error.response);
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
			} else{
				//输出来自服务器的异常信息
				ElementUI.Message.error({
					message: error.response.data.msg ? error.response.data.msg : "服务器没有提供此服务",
					duration: 6000,
					center: true
				});
			}
		} else { //没有连接上服务器的错误提示
			// this.$message.error({
			ElementUI.Message.error({
				message: "服务器无响应",
				duration: 6000,
				center: true
			});
		}
		
		//console.log(error);
		return Promise.reject();
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
