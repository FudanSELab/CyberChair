<template>
  <v-container>
    <v-btn
      class="primary"
      @click="backToReview()"
    >
      back
    </v-btn>
    <v-container>
  <v-row>
    <h2>Before Rebuttal</h2>
  </v-row>
  <v-row>
  <postlist
    @posterclicked="getTargetId"
    :_articleId="articleId"
    :_postStatus="postStatus.beforeRebuttal"
  >
  </postlist>
  </v-row>
    </v-container>
    <v-container
      v-if="afterRebuttal"
    >
  <v-row>
    <h2>After Rebuttal</h2>
  </v-row>
  <v-row>
    <postlist
      @posterclicked="getTargetId"
      :_articleId="articleId"
      :_postStatus="postStatus.afterRebuttal"
  >
  </postlist>
  </v-row>
    </v-container>
    <v-container
      style="background-color: rgb(232, 232, 232)"
    >

      <p
        class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
      >content
      </p>
      <v-row>
        <v-col
          lg="12"
          md="12"
          sm="12"
          class="my-0 py-0"
        >
          <v-textarea
            v-model="content"
            type="text"
            :validate-on-blur="true"
          >
          </v-textarea>
        </v-col>
      </v-row>
      <v-row
      >
        <v-col cols="6">
          <v-text-field
            type="text"
            v-model="targetName"
            :readonly="postIdReadOnly"
            label="reply to"
            hint="click the content you want to reply to"
            :validate-on-blur="true"
            prepend-icon="account_circle"
          ></v-text-field>
        </v-col>
        <v-col cols="6">
          <v-btn
            block
            class="green lighten-1"
            v-on:click="submitPost(rebuttalStatus)"
          >Post
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>
<script>
  import postlist from "./cyberchairComponents/post"
  export default {
    components:{
      postlist,
     // postComponent
    },

    data() {
      return {
        tips_text: '',
        articleId:"",
        targetName:"",
        postIdReadOnly:true,
        posterName:"",
        reviewStatus:"",
        meetingName:"",
        meetingStatus:"",
        postStatus:{
          beforeRebuttal:"beforeRebuttal",
          afterRebuttal:"afterRebuttal",
        },
        rebuttalStatus:"beforeRebuttal",
        afterRebuttal:false,
        targetId:"",
        content:"",
        rules: {
          required: value => !!value || 'This field is required.',
          targetId: function (val) {
            let reg = /^[0-9]*$/;
            if (reg.test(val)) return true;
            return false;
          }
        }
      }
    },

    methods: {
      backToReview:function(){
        this.$router.push({
          path: '/meeting/reviewArticle',
          query: {
            pcMemberName: this.posterName,
            articleId: this.articleId,
            meetingName: this.meetingName,
            meetingStatus: this.meetingStatus,
            reviewStatus: this.reviewStatus
          }});
      },
      getTargetId:function(item){
        this.targetId = item.postId;
        this.targetName = item.posterName;
      },
      loadParams:function(){
        this.articleId = this.$route.query.articleId;
        this.posterName= this.$route.query.posterName;
        this.rebuttalStatus = this.$route.query.rebuttalStatus;
        this.meetingStatus = this.$route.query.meetingStatus;
        this.reviewStatus = this.$route.query.reviewStatus;
        if(this.rebuttalStatus == "afterRebuttal")this.afterRebuttal = true;
        console.log(this.articleId);
        console.log(this.poster);
      },
      targetIdValidate:function(targetId) {
        if (targetId == "" || targetId == null) return true;
        let reg = /^[0-9]*$/;
        if (reg.test(targetId)) return true;
        return false;
      },
      contentValidate:function(content){
        return (!!content);
      },
      submitPost:function(status){
        if (this.targetId == "" || this.targetId == null)this.targetId = 0;
        if(!this.targetIdValidate(this.targetId)){
          this.tips_text = "The commentId must be a number";
          this.$toast(this.tips_text,{color:'red'});
          return;
        }
        if(!this.contentValidate(this.content)){
          this.tips_text = "The post content is required";
          this.$toast(this.tips_text,{color:'red'});
          return;
        }
        let requestUrl = "api/meeting/reviewPost";
        let formData = new FormData();
        formData.append("articleId",this.articleId);
        formData.append("targetId",this.targetId);
        formData.append("content",this.content);
        formData.append("status",this.postStatus);
        formData.append("posterName",this.posterName);
        let articleId = this.articleId;
        let targetId = this.targetId;
        let content = this.content;
        let posterName = this.posterName;
        this.$axios.post(requestUrl,
          {
            articleId: articleId,
            targetId:targetId,
            content:content,
            posterName:posterName,
            status:status
          }

        )
          .then(resp => {
            //console.log(resp)
            if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
              this.tips_text = "post successfully";
              this.$toast(this.tips_text,{color:'green'});
              window.setTimeout(function(){
                this.$router.go(0);
              }.bind(this), 1000);
            }else{
              this.tips_text = "error occurred due to " + resp.data.responseBody.reason;
              this.$toast(this.tips_text,{color:'red'})
            }
          })
          .catch(error => {
            this.tips_text = "error occurred when communicating with the backend";
            this.$toast(this.tips_text,{color:'red'})
          });

      },
    },

    created: function() {
      this.loadParams();
    },
  }

</script>

