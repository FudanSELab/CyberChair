<template>
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
        v-on:posterclicked="show"
        type="text"
        v-model="targetId"
        readonly="true"
        label="reply to"
        hint="the content id you want to reply"
        :rules="[rules.targetId]"
        :validate-on-blur="true"
        prepend-icon="account_circle"
      ></v-text-field>
    </v-col>
    <v-col cols="6">
      <v-btn
        block
        class="green lighten-1"
        v-on:click="submitPost(postStatus)"
      >Post
      </v-btn>
    </v-col>
  </v-row>
</v-container>
</template>
<script>
  export default {
    props:  [
      '_posterName',
      '_articleId',
      '_postStatus',
    ],

    data() {
      return {
        tips_text: '',
        articleId:this._articleId,
        posterName:this._posterName,
        postStatus:this._postStatus,
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
      show:function(){
        console.log("show");
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
              this.tips_text = status + " successfully";
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
    },
  }
</script>
