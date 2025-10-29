let baseUrl = ""; //这里是一个默认的url，可以没有
switch (process.env.NODE_ENV) {

	case 'development':

		baseUrl = "http://localhost:8080" //开发环境url

		break

		//     case 'ceshi':  // 注意这里的名字要和步骤二中设置的环境名字对应起来

		//         baseUrl = "http://localhost:3000/"  //测试环境中的url

		//         break

	case 'production':
	{ //协议
		let protocol = window.location.protocol;
		//主机
		let host = window.location.host;//带端口
		//let host = window.location.hostname;(不带端口)
		//动态请求地址            协议               主机	    
		baseUrl = protocol + "//" + host +"/ssm_demo";
	}
	break

}

export default baseUrl;
