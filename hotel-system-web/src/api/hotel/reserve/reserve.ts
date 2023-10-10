import request from "@/api/request";

export function getReserveListApi(params:object){
    return request({
        url:'reserve',
        method:'get',
        params
    })
}

export function getReserveApi(id:number){
    return request({
        url:`/reserve/${id}`,
        method:'get'
    })
}

export function unsubscribeApi(id:number){
    return request({
        url:`/reserve/unsubscribe/${id}`,
        method:'put'
    })
}

export function paymentApi(id:number){
    return request({
        url:`/reserve/payment/${id}`,
        method:'put'
    })
}
export function checkinApi(data:object){
    return request({
        url:'reserve/checkin',
        method:'post',
        data
    })
}