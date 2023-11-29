<template>
  <div class="hotel-footer">
    <div class="text-right">
    <dl>
      <dt>预订</dt>
      <dd><a>修改/取消订单</a></dd>
      <dd><a>检索酒店订单</a></dd>
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
    <img src="@/assets/1.jpg" width="200px" height="200px" >
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
  margin: 50px 0 0 ;
  padding: 20px 0 30px;
  line-height: 80px;
  text-align: center;
  color: #737573;
  border: 1px solid #e2e2e2;
  background-color: #333;
  height: 400px;
  position: relative;
}
.hotel-footer .text-right{
  color: #FFF;
  width: 65%;
  height: 300px;
  position: absolute;
}
.hotel-footer .text-right dl{

  display: block;
  float: left;
  margin: 30px 130px;
  padding: 2px;
}
.hotel-footer .text-right dl dt{
  font-weight: 700;
}
.hotel-footer .text-right dl dd{
  color: rgba(241, 237, 237, 0.541);
}
.hotel-footer .text-left{
  border-left: 1px solid white;
  width: 30%;
  height: 300px;
  margin-top: 20px;
  float: right;
}
.hotel-footer .text-left .min{
  margin-top: -20px;
  text-align: center;
  color: darkturquoise;
  font-size: larger;
}
.hotel-footer .footer{
  width: 100%;
  height: 80px;
  position: absolute;
  margin-top: 350px;
  background-color: black;
  float: left;
}
</style>