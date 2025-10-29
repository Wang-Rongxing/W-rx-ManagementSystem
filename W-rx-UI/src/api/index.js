import request from '../utils/request';
import {axiosExport} from '../utils/request';
export const fetchData = query => {
    return request({
        url: '/allUser',
        method: 'get',
        params: query
    });
};


export const ajaxGet = (url,query) => {
    return request({
        url: url,
        method: 'get',
        params: query
    });
};
export const ajaxDelete = (url,query) => {
    return request({
        url: url+'/'+query,
        method: 'delete',
    });
};
export const ajaxPost = (url,data) => {
    return request({
        url: url,
        method: 'post',
        data: data
    });
};

export const ajaxExport=(url,query)=>{
     return axiosExport({
		 url:url,
		 method:'get',
		 params:query
	 });	
};