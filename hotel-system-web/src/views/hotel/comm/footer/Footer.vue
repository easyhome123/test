<template>
  <div class="hotel-footer">
    <div class="text-right">
    <dl>
      <dt>预订服务</dt>
      <dd><router-link to="/hotel/myReserve">取消预订</router-link></dd>
      <dd><router-link to="/hotel/myReserve">检索预订</router-link></dd>
    </dl>
    <dl>
      <dt>客户服务</dt>
      <dd><a>常见问题</a></dd>
      <dd><a>联系我们</a></dd>
    </dl>
    <dl>
      <dt>了解更多</dt>
      <dd><a>旅游顾问</a></dd>
      <dd><a>公司旅行</a></dd>
    </dl>
   </div>
   <div class="text-left">
    <img src="@/assets/1.jpg">
    <p class="min">独家认证</p>
   </div>
  <div class="footer">
     @愿与星河同枕
  </div>
  </div>
</template>
<script setup lang="ts">

</script>
<style scoped>
.hotel-footer{
  display: flex;
  margin: 50px 0 0 ;
  padding: 20px 0 30px;
  color: #737573;
  border: 1px solid #e2e2e2;
  background-color: #333;
  height: 350px;
}
.hotel-footer .text-right{
  color: #FFF;
  width: 65%;
  height: 300px;
  line-height: 50px;
  display: flex;
}
.hotel-footer .text-right dl{
  display: block;
  width: 30%;
  margin: 50px 20px;
  text-align: center;
  padding: 2px;
}
.hotel-footer .text-right dl dt{
  font-weight: 700;
}
.hotel-footer .text-right dl dd{
  color: rgba(241, 237, 237, 0.541);
}
.hotel-footer .text-left{
  border-left: 2px dashed white;
  width: 30%;
  height: 300px;
  margin: 40px;
  text-align: center;
}
.hotel-footer .text-left img{
  width:200px;
  height:200px;
}
.hotel-footer .text-left .min{
  font-family: "宋体";
  padding: 20px;
  color: darkturquoise;
  font-size: larger;
}
.hotel-footer .footer{
  width: 100%;
  height: 40px;
  position: absolute;
  margin-top: 350px;
  background-color: black;
  text-align: center;
  padding-top: 20px;
}
</style>