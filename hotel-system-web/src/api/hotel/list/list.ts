import request from "@/api/request";

export function  getAllRoomTypeAndFloorApi(){
    return request({
        url:'hotel/getAllRoomTypeAndFloor',
        method:'get'
    })
}

export function getRoomListApi(params:object){
    return request({
        url:'hotel/getRoomList',
        method:'get',
        params
    })
}