import request from "@/api/request";

//获取入住列表函数
export function  getCheckinListApi(params:object){
    return request({
        url:'checkin',
        method:'get',
        params
    })
}

export function checkoutApi(id:number){
    return request({
        url:`checkin/checkout/${id}`,
        method:'put'
    })
}