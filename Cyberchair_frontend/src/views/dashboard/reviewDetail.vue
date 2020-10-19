<template>
  <v-list>
    <v-row
    >
      <v-col>
        <essay-detail
          :v-if="articleLoadCompleted"
          :articleId="articleId"
        >
        </essay-detail>
      </v-col>
    </v-row>
    <v-row
      v-if="!reviewHidden"
    >
      <v-col>
        <displayList
          :_title="title"
          :_display-set="displaySet"
          :_display-name="displayName"
          :_functions="functions"
          :_items="reviewList"
        >
      </displayList>
      </v-col>
    </v-row>
    <v-container
      style="background-color: rgb(232, 232, 232)"
      v-if="!rebuttalHidden"
    >
        <p
          class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
        >Rebuttal
        </p>
        <v-row>
          <v-col
            lg="12"
            md="12"
            sm="12"
            class="my-0 py-0"
          >
            <v-textarea
              v-model="rebuttal"
              type="text"
              :disabled="rebuttalReadOnly"
              :rules="[rules.required]"
              :validate-on-blur="true"
              v-on:change="$emit('rebuttalChange', rebuttal)"
            >
            </v-textarea>
          </v-col>
        </v-row>
      <v-row
        v-if="!rebuttalReadOnly"
      >
        <v-col cols="6"
          v-if="rebuttalNeeded"
        >
          <v-btn
            block
            class="green lighten-1"
            v-on:click="submitRebuttal('rebuttal')"
          >rebuttal
          </v-btn>
        </v-col>
        <v-col cols="6">
          <v-btn
            block
            class="grey lighten-1"
            v-on:click="submitRebuttal('giveUp')"
          >give up
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-list>
</template>


<script>
import displayList from "./cyberchairComponents/displayList"
import essayDetail from "./EssayDetail"

export default {
    components:{
        essayDetail,
        displayList,
    },

    data() {
        return {
          tips_text: '',
          loadComplete:false,
          articleLoadCompleted:false,
          authorName:"",
          rebuttalNeeded:false,
          meetingStatus:"",
          meetingName:"",
          articleId:-1,
          reviewList:[],
          reviewHidden:true,
          displaySet: ["score","confidence","review"],
          title:"Reviews",
          functions:[],
          displayName:"reviewStatus",
          rebuttal:"",
          rebuttalHidden:false,
          rebuttalReadOnly:false,
          rules:{
            required: value => !!value || 'This field is required.',
          }
        }


    },

    methods: {
        loadParams:function(){
          this.articleId = this.$route.query.articleId;
          this.authorName = this.$route.query.authorName;
          this.meetingName = this.$route.query.meetingName;
          this.meetingStatus = this.$route.query.meetingStatus;
          if(this.meetingStatus == "Reviewing" || this.meetingStatus == "ReviewCompleted" || this.meetingStatus == "ResultPublished") {
            this.reviewHidden = true;
            this.rebuttalHidden = true;
            this.rebuttalStatus = "beforeRebuttal";
          }else if(this.meetingStatus == "ReviewConfirmed") {
            this.reviewHidden = false;
            this.rebuttalHidden = false;
            this.rebuttalStatus = "beforeRebuttal";
          }else if(this.meetingStatus == "RebuttalFinish" || this.meetingStatus == "ReviewFinish" || this.meetingStatus =="ReviewPublish") {
            this.reviewHidden = false;
            this.rebuttalHidden = false;
            this.rebuttalStatus = "afterRebuttal";
            this.rebuttalReadOnly = true;
          }
          this.loadReviews();
          this.loadRebuttal();
        },
        submitRebuttal:function(status){
          let articleId = this.$route.query.articleId;
          let content = this.rebuttal;
          let requestUrl = "api/meeting/rebuttal";
          let that = this;
          this.$axios.post(requestUrl ,
            {
              articleId:articleId,
              content:content,
              status:status
            })
            .then(resp => {
              //console.log(resp)
              if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                this.tips_text = status + " successfully";
                this.$toast(this.tips_text,{color:'green'});
                window.setTimeout(function(){
                  that.$router.push({
                    path: '/submission/list',
                    query: {
                      authorName: that.authorName,
                      meetingName: that.meetingName,
                    }});
                }.bind(that), 1000);
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
        loadRebuttal:function() {
          this.articleId = this.$route.query.articleId;
          let that = this;
          let requestUrl = "api/meeting/rebuttalInfo";
          this.$axios.get(
            requestUrl,
            {params: {
                articleId: this.articleId,
              }}
          )
            .then(resp => {
              if(resp.data.responseCode == 200){
                if( resp.data.responseMessage == "success") {
                  let rebuttal = resp.data.responseBody.rebuttal;
                  //console.log("\n\n\n\n\n" + rebuttal);
                  if (resp.data.responseBody === undefined) that.rebuttalReadOnly = false;
                  else that.rebuttalReadOnly = true;
                  that.rebuttal = rebuttal.content;
                  console.log(that.rebuttal);
                }
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
        loadReviews:function(){
          this.articleId = this.$route.query.articleId;
          this.articleLoadCompleted = true;
          let that = this;
          let requestUrl = "api/user/reviews";
          this.$axios.get(
            requestUrl,
            {params: {
                articleId: this.articleId,
              }}
          )
            .then(resp => {
              //console.log(resp)
              if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                let reviews = resp.data.responseBody.reviews;
                for(let i = 0; i < reviews.length; i++) {
                  if(reviews[i]["score"] < 0)
                  {
                    that.rebuttalNeeded = true;
                    reviews[i]["reviewStatus"] = "Rejected";
                  }
                  else if(reviews[i]["score"] > 0)reviews[i]["reviewStatus"] = "Accepted";
                  that.reviewList.push(reviews[i]);
                }
              }else{
                this.tips_text = "error occurred due to " + resp.data.responseBody.reason;
                this.$toast(this.tips_text,{color:'red'})
              }
              that.loadComplete = true;
            })
            .catch(error => {
              this.tips_text = "error occurred when communicating with the backend";
              this.$toast(this.tips_text,{color:'red'})
            });

        }

    },

    created: function() {
        this.loadParams();

    },
}
</script>

<style scoped>
#essayDetail{
    background-color: rgb(232, 232, 232) !important;
}
</style>
