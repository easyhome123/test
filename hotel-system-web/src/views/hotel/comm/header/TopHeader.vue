<template>
  <div class="hotel-header hotel-store">
    <div class="hotel-container">
      <router-link class="logo" to="/index">
        <img src="@/assets/hotel/images/hotel-logo.png" alt="酒店管理系统"/>
        <p>愿与星河同枕</p>
      </router-link>

    

      <div class="hotel-component"></div>

      <ul class="hotel-nav">
        <li class="hotel-nav-item" :class="{'hotel-active':hotelActive=='/index'}">
          <router-link to="/index">首页</router-link>
        </li>

        <li class="hotel-nav-item" :class="{'hotel-active':hotelActive=='/hotel/list'}">
          <router-link to="/hotel/list">全部房间</router-link>
        </li>
<!--        已登录时-->
        <template v-if="memberInfo.memberName!='' ">
          <li class="hotel-nav-item hotel-member" :class="{'hotel-active':hotelActive=='/hotel/myInfo'}">
            <router-link class="hotel-nav-avatar hotel-case-active" to="/hotel/myInfo">
              <img v-if="memberInfo.memberIcon!= null " :src="url+'uploadFile/'+memberInfo.memberIcon" alt="">
              <img v-else src="@/assets/hotel/images/head.jpg" alt="">
              <cite>欢迎您：{{memberInfo.memberName}}</cite>
            </router-link>
          </li>
          <li class="hotel-nav-item">
            <a class="hotel-case-active" @click="exitSystem" href="javaScript:void (0);">退出</a>
          </li>
        </template>

<!--        未登录时-->
        <template v-else>
          <li class="hotel-nav-item" :class="{'hotel-active':hotelActive=='/hotel/login'}">
            <router-link to="/hotel/login">马上登录</router-link>
          </li>
          <li class="hotel-nav-item" :class="{'hotel-active':hotelActive=='/hotel/register'}">
            <router-link to="/hotel/register">立即注册</router-link>
          </li>
        </template>

        <li class="hotel-nav-item"  >
          <router-link to="/home"  target="_blank">进入后台</router-link>
        </li>
      </ul>
    </div>
  </div>

</template>
<script setup lang="ts">
import {useRoute} from "vue-router";
import {ref} from "vue";
import {useMemberStore} from "@/store/modules/member";
import {ElMessage, ElMessageBox} from "element-plus";

const route = useRoute()
const hotelActive = ref()
hotelActive.value = route.path

const { memberInfo } = useMemberStore()
//服务器路径
const url = import.meta.env.VITE_APP_BASE_API

const exitSystem = ()=>{
  ElMessageBox.confirm(
      '您确定要退出酒店管理系统?',
      '温馨提示',
      {
        confirmButtonText:'确定',
        cancelButtonText:'取消',
        type:'warning'
      }
  ).then(()=>{
    ElMessage.success("退出成功")
    window.localStorage.removeItem("memberStore")
    window.location.href = '/'
  }).catch(()=>{
    ElMessage.error("退出失败")
  })
}
</script>
<style scoped>
.hotel-header{
  height: 60px;
  border-bottom: 1px solid skyblue;
  background-color: skyblue;
  z-index: 1000;
  position: relative;
}

.hotel-container{
  width: 1300px;
  padding: 0;
  position: relative;
  margin: 0 auto;
  box-sizing: border-box;
}

.hotel-container .logo{
  left: 15px;
  position: absolute;
  top: 15px;
}

.hotel-container .logo img{
  width: 80px;
  height: 30px;
  border: none;
}
.hotel-container .logo p{
  display: inline-block;
  font-size: 15px;
  padding: 5px;
  color: #fff;
}

.hotel-container .hotel-nav{
  right: 15px;
  position: absolute;
  top: 0;
  padding: 0;
  background: none;
  color: #fff;
  border-radius: 2px ;
  box-sizing: border-box;
}

.hotel-nav .hotel-nav-item{
  position: relative;
  display: inline-block;
  vertical-align: middle;
  line-height: 60px;
  margin: 0 20px;
}

.hotel-nav *{
  font-size: 15px;
}

.hotel-nav .hotel-nav-item a{
  display: block;
  padding:0 20px;
  color: white;
  transition:  all .3s;
  -webkit-transition: all .3s;

}

.hotel-active{
  color: white!important;
}

.hotel-nav .hotel-nav-item a:hover .hotel-active a{
  color: white;
}

.hotel-nav .hotel-active:after{
  top: auto;
  bottom: 0;
  width: 100%;
  position: absolute;
  left: 0;
  height: 5px;
  background-color: #5FB878;
  transition: all .2s;
  -webkit-transition: all .2s;
}
.hotel-nav-avatar img{
  width: 35px;
  height: 35px;
  margin-left: 10px;
  margin-right: 10px;
  border-radius: 100%;
  vertical-align: middle;

}
</style>