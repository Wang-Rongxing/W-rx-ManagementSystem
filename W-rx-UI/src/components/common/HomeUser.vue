<template>
    <div class="wrapper">
        <v-head></v-head>
      <div>
        <router-view></router-view>
      </div>
    </div>
</template>

<script>
import vHead from './HeaderUser.vue';
// import vSidebar from './Sidebar.vue';
// import vTags from './Tags.vue';
// import bus from './bus';
export default {
    components: {
        vHead,
    },
};
</script>
