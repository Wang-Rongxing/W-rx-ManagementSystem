<template>
  <div style="text-align: center;background-color: #f1f1f3;height: 100%;padding: 0px;margin: 0px;">
    <h1 style="font-size: 50px;">你好，欢迎使用本系统</h1>
    <el-descriptions  title="个人中心" :column="2" size="40" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-custom"></i>
          账号
        </template>
        {{this.employee.name}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          角色
        </template>
        {{roles}}
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
     role: '',
     employee: JSON.parse(sessionStorage.getItem('user'))
   };
  },
  computed: {
    roles() {
      const roles = this.employee.roles;
      // 检查roles是否为数组且包含元素
      if (Array.isArray(roles) && roles.length > 0) {
        // 遍历roles数组查找匹配的角色
        for (let role of roles) {
          if (role.includes('manager')) {
            return '经理';
          } else if (role.includes('roomattendant')) {
            return '客房管理员';
          } else if (role.includes('reception')){
            return '前台';
          }
        }
        // 如果没有找到经理角色，返回第一个角色的中文名称
        const firstRole = roles[0];
        if (firstRole.includes('admin')) {
          return '管理员';
        }
        return '普通用户';
      }
      return '未知角色';
    }
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
