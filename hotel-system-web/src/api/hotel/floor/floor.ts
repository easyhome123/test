//获取楼层列表数据

import request from "@/api/request";

export  function  getFloorListApi(params:object){
    return request({
        url:'floor',
        method:'get',
        params
    })
}

export  function  addFloorApi(data:object){
    return request({
        url:'floor',
        method:'post',
        data
    })
}

export  function getFloorApi(id:number){
    return request({
        url:`floor/${id}`,
        method:"get"
    })
}

export  function  editFloorApi(data:Object){
    return request({
        url:'floor',
        method: "put",
        data
    })
}

export  function  delFloorApi(id:number){
    return request({
        url:`floor/${id}`,
        method:'delete',
    })
}