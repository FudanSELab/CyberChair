<!--

props
articleId=${long}
postStatus=${string} (beforeRebuttal / afterRebuttal)

usage:
<postlist
  articleId="666"
  postStatus="beforeRebuttal"
>
</postlist>


events that will fire:
when click any post, a @post-click event will be fired
and will send out a
post:{
  targetId
  username
  time
  content
}
-->

<template>
<v-container>
  <v-card>
    <v-list three-line>
        <v-list-item-group>
      <template v-for="(item, index) in items">
        <v-list-item :key="item.postId" v-on:click="posterclick(item)">
          <template v-slot:default="{active}">

            <v-list-item-action>
              <v-list-item-action-text v-text="item.timeStamp"></v-list-item-action-text>
              <v-icon v-if="!active" color="grey lighten-1">star_border</v-icon>

              <v-icon v-else color="yellow">star</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title v-text="item.posterName"></v-list-item-title>
              <v-list-item-subtitle class="text--primary"><span>{{item.targetContent}}</span></v-list-item-subtitle>
              <v-list-item-subtitle>{{item.postContent}}</v-list-item-subtitle>
            </v-list-item-content>



          </template>
        </v-list-item>

        <v-divider v-if="index + 1 < items.length" :key="index"></v-divider>
      </template>
        </v-list-item-group>
    </v-list>
  </v-card>
</v-container>
</template>

<script>
export default {
  props:  [
    '_articleId',
    '_postStatus'

  ],

  data() {
    return {
      items: [],
      articleId: this._articleId,
      postStatus: this._postStatus,
    }
  },

  methods: {
    loadPostList() {
      let requestUrl = "api/meeting/postList"
      this.$axios.get(
        requestUrl,
        {
          params: {
            articleId: this.articleId,
            postStatus:this.postStatus
          }
        }
      ).then(resp => {
        if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
          this.items = resp.data.responseBody.postlist
        }else{
          this.$toast("backend error occurred, please refresh", {color: "red"})
        }
      }).catch(err => {
        this.$toast("an unexpected error occurred, please refresh",{color:'red'})
      })

    },

    posterclick(item) {
      this.$emit('posterclicked',item);
      console.log(item);
      console.log("posterclicked emitted")
    }


  },

  created() {
    this.loadPostList()
  }
};
</script>
