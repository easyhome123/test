import request from "@/api/request";

//获取首页数据
export function getIndexApi(){
    return request({
        url:'hotel/getIndex',
        method:"get"
    })
}

export function registerApi(data:object){
    return request({
        url:'hotel/register',
        method:'post',
        data
    })
}

export function loginApi(data:object){
    return request({
        url:'hotel/login',
        method:'post',
        data
    })
}
