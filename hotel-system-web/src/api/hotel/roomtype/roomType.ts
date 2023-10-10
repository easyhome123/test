import request from "@/api/request";

export  function  getRoomTypeListApi(params:object){
    return request({
        url: 'roomType',
        method: 'get',
        params
    })
}

//新增房间类型
export  function  addRoomTypeApi(data:object){
    return request({
        url:'roomType',
        method:'post',
        data
    })
}

//根据id获取房间类型信息
export  function  getRoomTypeApi(id:number){
    return request({
        url:`roomType/${id}`,
        method:'get'
    })
}

export function  delRoomTypeApi(id:number){
    return request({
        url: `roomType/${id}`,
        method: 'delete'
    })
}

export  function  editRoomTypeApi(data:object){
    return request({
        url:'roomType',
        method:'put',
        data
    })
}