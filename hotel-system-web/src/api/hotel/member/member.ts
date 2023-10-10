import request from "@/api/request";

export function getMemberListApi(params:object){
    return request({
        url: 'member',
        method:'get',
        params
    })
}

export function getMemberApi(id:number){
    return request({
        url:`member/${id}`,
        method:'get'
    })
}

export function  rechargeApi(data:object){
    return request({
        url:'member/recharge',
        method:'put',
        data
    })
}

export function deleteMemberApi(id:number){
    return request({
        url:`member/${id}`,
        method:'delete'
    })
}

//获取登录会员信息
export function  getLoginMemberInfoApi(){
    return request({
        url:'hotel/getLoginMemberInfo',
        method:'get'
    })
}

export function updateMemberApi(data:object){
    return request({
        url:'hotel/updateMember',
        method:'put',
        data
    })
}

export  function  getMyReserveListApi(params:object){
    return request({
        url:'hotel/getMyReserveList',
        method:'get',
        params
    })
}

export function paymentApi(id:number){
    return request({
        url:`hotel/payment/${id}`,
        method:'put'
    })
}

export function getMyRechargeRecordListApi(params:object){
    return request({
        url:'hotel/getMyRechargeRecordList',
        method:'get',
        params
    })
}

export function updateMemberPwdApi(data:object){
    return request({
        url:'hotel/updateMemberPwd',
        method:'put',
        data
    })
}