<template>
  <div class="member">
    <dl>
      <dt>会员中心</dt>
      <dd :class="{ activate:activate == '/hotel/myInfo'}">
        <router-link to="/hotel/myInfo">我的资料</router-link>
      </dd>
      <dd :class="{ activate:activate == '/hotel/myReserve'}">
        <router-link to="/hotel/myReserve">我的预定</router-link>
      </dd>
      <dd :class="{ activate:activate == '/hotel/payment'}">
        <router-link to="/hotel/payment">充值记录</router-link>
      </dd>
      <dd :class="{ activate:activate == '/hotel/updatePwd'}">
        <router-link to="/hotel/updatePwd">修改密码</router-link>
      </dd>
    </dl>
  </div>
</template>
<script setup lang="ts">
import {ref} from "vue";
import {useRoute} from "vue-router";

const activate = ref()
const route = useRoute()
activate.value =route.path

</script>

<style scoped>
.member{
  width: 300px;
  z-index: 100;
  top: -60px;
  background: white;
  box-shadow: 0 1px 5px rgb(0 0 0 /15%);
}
.member dl dt{
  display: block;
  font-size: 26px;
  background: #5e6eba;
  line-height: 60px;
}
.member dl dt a{
  color: #fff;
}
.member dl dd{
  display: block;
  width: 100%;
  margin: 0;
  line-height: 60px;
  border: none;
  border-bottom: 1px solid #f0f0f0;
}
.activate{
  background-color: #b3bde8;

}
a {
  color: #666;
  text-decoration: none;
}
a:hover {
  color: #2fa7b9;
}
</style>