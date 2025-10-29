<template>
  <div style="text-align: center;background-color: #f1f1f3;height: 100%;padding: 0px;margin: 0px;">
    <h1 style="font-size: 50px;">你好，欢迎使用本系统</h1>
    <el-descriptions  title="个人中心" :column="2" size="40" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-custom"></i>
          账号
        </template>
        {{this.employee.username}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          注册时间
        </template>
        {{this.employee.phone}}
      </el-descriptions-item>

    </el-descriptions>

    <DateUtils></DateUtils>
  </div>
</template>

<script>
import DateUtils from "./DateUtils";
export default {
  components: {DateUtils},
  data(){
   return{
     employee: JSON.parse(sessionStorage.getItem('user'))
   };
  },
};
</script>


<style scoped>
.el-descriptions{
  width:90%;

  margin: 0 auto;
  text-align: center;
}
</style>
