// 1、导入Vue Router模块
import { createRouter,createWebHashHistory } from 'vue-router'
import Nprogress from "../config/nprogress";
import {useUserStore} from "../store/modules/user";
import {useMenuStore} from "../store/modules/menu";
import {useMemberStore} from "../store/modules/member";
// 定义一些路由，每一个都需要映射到一个组件

// 定义静态路由
export const staticRouter = [
    {
        path: '/',
        redirect: '/index',
        isMenu: false
    },
    {
      path: '/index',
      name: 'Index',
      isMenu: false,
      meta: {
          title: '酒店管理系统-首页'
      },
        component:()=>import('@/views/hotel/index/Index.vue')
    },
    {
        path: '/hotel',
        name: 'Hotel',
        redirect: '/hotel/list',
        isMenu: false,
        children: [
            {
                path: 'list',
                name: 'List',
                meta: {
                    title: '全部房间列表'
                },
                component:()=>import('@/views/hotel/list/List.vue')
            },
            {
                path: 'register',
                name: 'Register',
                meta: {
                    title: '会员注册',
                },
                component:()=>import('@/views/hotel/register/Register.vue')
            },
            {
                path: 'login',
                name: 'MemberLogin',
                meta: {
                    title: '会员登录',
                },
                component:()=>import('@/views/hotel/register/Login.vue')
            },
            {
                path: 'details/:id',
                name: 'Details',
                meta: {
                    title: '详情页',
                },
                component:()=>import('@/views/hotel/details/Details.vue')
            },
            {
                path: 'myInfo',
                name: 'MyInfo',
                meta: {
                    title: '个人信息',
                },
                component:()=>import('@/views/hotel/member/MyInfo.vue')
            },
            {
                path: 'myReserve',
                name: 'MyReserve',
                meta: {
                    title: '我的预订',
                },
                component:()=>import('@/views/hotel/member/MyReserve.vue')
            },
            {
                path: 'payment',
                name: 'Payment',
                meta: {
                    title: '我的充值记录'
                },
                component:()=>import('@/views/hotel/member/MyRechargeRecord.vue')
            },
            {
                path: 'updatePwd',
                name: 'UpdatePwd',
                meta: {
                    title: '充值记录'
                },
                component:()=>import('@/views/hotel/member/UpdatePwd.vue')
            }
        ]

    },
    {
        path: '/login',
        name: 'Login',
        meta: { title: '后台管理系统-后台登录' },
        component: ()=> import('@/views/system/login/Login.vue'),
        isMenu: false
    },
    {
        path: '/homeindex',
        name: 'homeindex',
        component: ()=> import('@/views/system/layout/Index.vue'),
        redirect: '/home',
        isMenu: true,
        funcNode: 1,
        children: [
            {
                path: '/home',
                name: 'home',
                meta: { title: '首页',icon: 'House',affix: true},
                component: ()=> import('@/views/system/home/Index.vue'),
            }
        ]

    },
    {
        path: '/user',
        name: 'userSetting',
        redirect: '/user/setting',
        component: ()=>import('@/views/system/layout/Index.vue'),
        isMenu: true,
        funcNode: 1,
        children: [
            {
                path: 'setting',
                name: 'PersonalSettings',
                meta: { title: '个人设置',icon: 'Basketball' },
                component: ()=> import('@/views/system/user/components/PersonalSettings.vue')
            }
        ]
    }

]

// 定义动态路由
export const asyncRoutes = [
    {
        path: '/base',
        name: 'Base',
        meta: {
            title: '数据管理',
            icon: 'DataAnalysis',
            role: ['ROLE_ADMIN']
        },
        redirect: '/base/roomtype',
        component: ()=> import('@/views/system/layout/Index.vue'),
        isMenu: true,
        funcNode: 2,
        children: [
            {
                path: 'roomtype',
                name: 'RoomType',
                meta: {
                    title: '房间类型管理',
                    icon: 'Memo',
                    role: ['ROLE_ADMIN']
                },
                component: ()=> import('@/views/hotel/roomtype/RoomTypeList.vue')

            },
            {
                path: 'floor',
                name: 'Floor',
                meta: {
                    title: '楼层管理',
                    icon: 'School',
                    role: ['ROLE_ADMIN']
                },
                component: ()=> import('@/views/hotel/floor/FloorList.vue')
            },
            {
                path: 'room',
                name: 'Room',
                meta: {
                    title: '房间管理',
                    icon: 'Box',
                    role: ['ROLE_ADMIN']
                },
                component: ()=> import('@/views/hotel/room/RoomList.vue')
            },
            {
                path: 'member',
                name: 'Member',
                meta: {
                    title: '会员管理',
                    icon: 'Avatar',
                    role: ['ROLE_ADMIN','ROLE_USER']
                },
                component: ()=> import('@/views/hotel/member/MemberList.vue')
            },
            {
                path: 'roomstate',
                name: 'RoomState',
                meta:{
                    title:'房态管理',
                    icon: 'House' ,
                    role: ['ROLE_ADMIN']
                },
                component: ()=>import("@/views/hotel/roomstate/RoomState.vue")   
            },
        ]
    },
    {
        path: '/reserve',
        name: 'Reserve',
        meta: {
            title: '预定管理',
            icon: 'GoldMedal',
            role: ['ROLE_ADMIN','ROLE_USER']
        },
        redirect: '/reserve/index',
        component: ()=>import('@/views/system/layout/Index.vue'),
        isMenu: true,
        funcNode: 2,
        children: [
            {
                path: 'index',
                name: 'ReserveIndex',
                meta: {
                    title: '房间预定管理',
                    icon: 'Money',
                    role: ['ROLE_ADMIN','ROLE_USER']
                },
                component:()=>import('@/views/hotel/reserve/ReserveList.vue')
            },
            {
                path: 'checkin',
                name: 'Checkin',
                meta: {
                    title: '入住管理',
                    icon: 'Tickets',
                    role: ['ROLE_USER','ROLE_ADMIN']
                },
                component: ()=>import('@/views/hotel/checkin/CheckinList.vue')
            },
        ]
    },
    {
        path: '/system',
        name: 'system',
        meta: {
            title: '系统管理',
            icon: 'GoldMedal',
            role: ['ROLE_ADMIN']
        },
        redirect: '/system/user',
        component: ()=> import('@/views/system/layout/Index.vue'),
        isMenu: true,
        funcNode: 2,
        children: [
            {
                path: 'user',
                name: 'user',
                meta: {
                    title: '用户管理',
                    icon: 'UserFilled',
                    role: ['ROLE_ADMIN']
                },
                component: ()=> import('@/views/system/user/UserList.vue')
            },
            {
                path: 'role',
                name: 'Role',
                meta: {
                    title: '角色管理',
                    icon: 'Stamp',
                    role: ['ROLE_ADMIN']
                },
                component: () => import('@/views/system/role/RoleList.vue')
            },
            {
                path: 'log',
                name: 'Log',
                meta: {
                    title: '日志管理',
                    icon: 'Histogram',
                    role: ['ROLE_ADMIN']
                },
                component: () => import('@/views/system/log/LogList.vue')
            }
        ]
    }
]


// 3、创建路由实例并传递‘routes’配置
const router = createRouter({
    history: createWebHashHistory(),
    routes: staticRouter
})

// 设置白名单
const whiteList = ['/login','/index/','/hotel/list','/hotel/register','hotel/details']
// 路由拦截守卫
router.beforeEach(async (to,from,next)=> {
    // 1.Nprogress 开始
    Nprogress.start()

    // 2.设置标题
    if(typeof(to.meta.title) === 'string'){
        document.title = to.meta.title ||'后台管理系统'
    }

    // 3.如果是白名单的路径，直接放行
    const some = whiteList.some(function (item) {
        return to.path.indexOf(item)!==-1
    })
    //4. 白名单直接放行
    if (some) {
        next()
    }else{
        // 5.判断访问路径是前台还是后台
        if(to.path.indexOf('hotel')!==-1){
            // 6.判断前台是否有token，没有重定向login
            const memberStore = useMemberStore()
            // 已经登录,直接放行
            if(memberStore.memberToken!=''){
                next()
            // 未登录，跳转到前台登录页
            }else{
                return next({path:`/hotel/login?redirect=${to.path}`,replace:true})
            }
        }else {
            // 7.判断是否有token，没有重定向login
            const userStore = useUserStore()
            if(userStore.token!=''){
                // 获取登录用户的角色
                const { userInfo } =userStore
                const roles = []
                // @ts-ignore
                roles.push(userInfo.role.code)

                // 根据角色动态生成路由访问映射
                const menuStore = useMenuStore()

                if(!menuStore.routers.length){
                    const accessRoutes = menuStore.generateRouter({roles: roles})
                    accessRoutes.forEach(item => router.addRoute(item))
                    next({...to,replace:true})
                }else {
                    // 正常访问页面
                    next()
                }

            }else {
                return next({path: `/login?redirect=${to.path}`,replace:true})
            }

        }

    }

})

// 路由跳转结束
router.afterEach(()=> {
    Nprogress.done()
})

// 路由跳转失败
router.onError(error=> {
    Nprogress.done()
    console.warn("路由错误",error.message)
})

export default router
