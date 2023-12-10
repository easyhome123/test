import request from "@/api/request";

export function getRoomListApi(params: Object) {
    return request({
        url: 'room',
        method: 'get',
        params
    })

}
export function addRoomApi(data: object) {
    return request({
        url: 'room',
        method: 'post',
        data
    })
}

export function getAllRoomTypeListApi() {
    return request({
        url: 'roomType/all',
        method: 'get'
    })
}
export function getAllFloorListApi() {
    return request({
        url: 'floor/all',
        method: 'get'
    })
}

export function getRoomApi(id: number) {
    return request({
        url: `room/${id}`,
        method: 'get'
    })
}

export function editRoomApi(data: object) {
    return request({
        url: 'room',
        method: 'put',
        data
    })
}

export function delRoomApi(id: number) {
    return request({
        url: `room/${id}`,
        method: 'delete'
    })
}

export function cleanRoomApi (id:number){
   return request({
        url: `room/${id}`,
        method:'put',
    })
}
   