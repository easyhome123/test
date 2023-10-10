import request from "@/api/request";

export function getRoomDetailApi(id:number){
    return request({
        url:`hotel/detail/${id}`,
        method:'get'
    })
}

export  function getDatesByRoomIdApi(id:number){
    return request({
        url:`hotel/getDatesByRoomId/${id}`,
        method:'get'
    })
}
export function  addReserveApi(data:object){
    return request({
        url:'hotel/addReserve',
        method:'post',
        data
    })
}