<template>
    <postlist _articleId='6' _postStatus="beforeRebuttal" v-on:posterClick="clickPost($event)">
    </postlist>
</template>
<script>
import postlist from './dashboard/cyberchairComponents/post'

export default {
    components: { postlist },

    data() {
    },

    methods: { clickPost(item){
        console.log(item)
    } },
    
}
</script>