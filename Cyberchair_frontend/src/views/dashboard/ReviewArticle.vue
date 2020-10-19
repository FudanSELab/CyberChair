<template>
  <v-list
    id="reviewArticle"
    class="lighten-5 px-10 mx-5 align-center"
    style="background-color: rgb(232, 232, 232)"
  >

    <v-row
      :v-if="articleLoadCompleted"
    >
        <v-col>
          <essay-detail
          :article-id="articleId"
          >
          </essay-detail>
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
            :disabled="true"
            :validate-on-blur="true"
          >
          </v-textarea>
        </v-col>
      </v-row>
    </v-container>
    <v-row
      v-if="!discussionHidden"
    >
      <v-col
        class="my-2 py-2 align-center"
      >
        <v-btn
          color="primary"
          v-on:click="jumpToDiscussion()"
        >
          Discussion
        </v-btn>
      </v-col>
    </v-row>
    <v-row
    >
      <v-col lg="3" md="4" sm="12" class="my-0 py-0">
        <v-textarea
          v-model="comment"
          type="text"
          label="Comment"
          hint=""
          _hint="Write your comment"
          :readonly="reviewReadOnly"
          :validate-on-blur="true"
          :rules="[rules.comment]"
        >
        </v-textarea>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
      <v-rating
        v-model="markRating.rating"
        :length="markRating.length"
        :empty-icon="markRating.emptyIcon"
        :full-icon="markRating.fullIcon"
        :half-icon="markRating.halfIcon"
        :half-increments="markRating.halfIncrements"
        :hover="markRating.hover"
        :readonly="reviewReadOnly"
        :size="markRating.size"
        :dense="markRating.dense"
        :color="markRating.color"
        :background-color="markRating.bgColor"
      ></v-rating>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <span class="caption text-uppercase">mark:</span>
        <span class="font-weight-bold">
          {{ markSet[markRating.rating]['mark']}}({{ markSet[markRating.rating]['status']}})
        </span>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-rating
        v-model="confidenceRating.rating"
        :length="confidenceRating.length"
        :empty-icon="confidenceRating.emptyIcon"
        :full-icon="confidenceRating.fullIcon"
        :half-icon="confidenceRating.halfIcon"
        :half-increments="confidenceRating.halfIncrements"
        :hover="confidenceRating.hover"
        :readonly="reviewReadOnly"
        :size="confidenceRating.size"
        :dense="confidenceRating.dense"
        :color="confidenceRating.color"
        :background-color="confidenceRating.bgColor"
      ></v-rating>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <span class="caption text-uppercase">confidence:</span>
        <span class="font-weight-bold">
          {{ confidenceSet[confidenceRating.rating] }}
        </span>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        v-if="!reviewReadOnly"
      >
      <v-btn
        color="primary"
        class="align-center"
        v-on:click="commitReview()"
        >{{ submitButtonDisplay }}
      </v-btn>
      </v-col>
      <v-col
        v-if="!confirmHidden"
      >
      <v-btn
        color="primary"
        class="align-center"
        v-on:click="confirmReview()"
      >confirm review
      </v-btn>
      </v-col>
    </v-row>
  </v-list>
</template>


<script>
  import essayDetail from "./EssayDetail"

  export default {
    components:{
      essayDetail
    },
    // props:[
    //   "articleId"
    // ],

    data() {
      return {
        discussionHidden:false,
        articleLoadCompleted:false,
        reviewReadOnly:true,
        reviewStatus:"",
        meetingStatus:"",
        meetingName:"",
        tips_text: '',
        loadComplete:false,
        articleId:-1,
        pcMemberName:"",
        comment:"",
        submitButtonDisplay:"Commit Review",
        rebuttal:"",
        rebuttalStatus:"beforeRebuttal",
        rebuttalHidden: false,
        confirmHidden: false,
        reviewArticle:{
          title: "",
          abstract: "",
          submitTime: "",
          pdfPath:"",
          topics:[]
        },
        essayDetail:{
          essayTitle: "",
          essayAbstract: "",
          essayPDFUrl: ""
        },
        page:1,
        pageCount:-1,
        markSet:{
          "1":{
            mark:-2,
            status:"reject"
          },
          "2":{
            mark:-1,
            status:"week reject"
          },
          "3":{
            mark:1,
            status:"week accept"
          },
          "4":{
            mark:2,
            status:"accept"
          },
        },
        reMarkSet:{
          "-2":1,
          "-1":2,
          "1":3,
          "2":4
        },
        confidenceSet:{
          "1":"very low",
          "2":"low",
          "3":"high",
          "4":"very high",
        },
        reConfidenceSet:{
          "very low":1,
          "low":2,
          "high":3,
          "very high":4
        },
        markRating:{
          emptyIcon: 'mdi-star-outline',
          fullIcon: 'mdi-star',
          halfIcon: 'mdi-star-half-full',
          halfIncrements: false,
          hover: true,
          length: 4,
          rating: 1,
          readonly: false,
          size: 64,
          dense: false,
          color: 'primary',
          bgColor: 'grey lighten-1',
        },
        confidenceRating:{
          emptyIcon: 'mdi-star-outline',
          fullIcon: 'mdi-star',
          halfIcon: 'mdi-star-half-full',
          halfIncrements: false,
          hover: true,
          length: 4,
          rating: 1,
          readonly: false,
          size: 64,
          dense: false,
          color: 'primary',
          bgColor: 'grey lighten-1',
        },
        rules:{
          comment:function(text){
            if(text != "")
              return true;
            else
              return "Comment is required";
          }

        }
      }


    },

    methods: {
      jumpToDiscussion:function(){
        let articleId = this.articleId;
        let rebuttalStatus = this.rebuttalStatus;
        let posterName = this.pcMemberName;
        let that = this;
        this.$router.push({
          path: '/article/discussion',
          query: {
            articleId:articleId,
            posterName: posterName,
            rebuttalStatus:rebuttalStatus,
            meetingStatus: that.meetingStatus,
            reviewStatus: that.reviewStatus
          }
        })
      },
      confirmReview:function(){
        let that = this;
        let pcMemberName = this.pcMemberName;
        let articleId = this.articleId;
        let status = this.rebuttalStatus;
        let requestUrl = "api/meeting/reviewConfirm";
        this.$axios.post(
          requestUrl,
          {
            articleId: articleId,
            pcMemberName: pcMemberName,
            status: status
          }
        )
          .then(resp => {
            if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
              this.tips_text = "confirm review successfully";
              this.$toast(this.tips_text,{color:'green'})
              window.setTimeout(function(){
                that.$router.push({
                  path: '/meeting/reviewArticles',
                  query: {
                    pcMemberName: that.pcMemberName,
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
      commitReview:function(){
        let pcMemberName = this.pcMemberName;
        let articleId = this.articleId;
        let reviews = this.comment;
        let score = this.markSet[this.markRating['rating']]['mark'];
        let confidence = this.confidenceSet[this.confidenceRating['rating']];
        let status = this.rebuttalStatus;
        let requestUrl = "api/meeting/reviewer";
        let getJson =           {
          articleid: articleId,
          pcMemberName:pcMemberName,
          score:score,
          confidence:confidence,
          reviews:reviews
        };

        if(this.reviewStatus != "unReviewed")
        {
          requestUrl = "api/meeting/updateReview"
          getJson =           {
            articleId: articleId,
            pcMemberName:pcMemberName,
            score:score,
            confidence:confidence,
            reviews:reviews,
            status:status
          };
        }
        if(typeof(reviews)=="undefined" || ""+reviews =="undefined"  || reviews == null || reviews == "") {
          this.tips_text = "Please add some comments";
          this.$toast(this.tips_text,{color:'red'})
          return false;
        }
        this.$axios.post(
          requestUrl,
          getJson
        )
          .then(resp => {
            if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
              this.tips_text = "commit review successfully";
              this.$toast(this.tips_text,{color:'green'})
              let jumpUrl = "/meeting/reviewArticles?pcMemberName=" + this.pcMemberName + "&meetingName=" + this.meetingName;
              window.setTimeout(function(){
                this.$router.replace(jumpUrl);
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
      loadParams: function(){
        this.articleId = this.$route.query.articleId;
        this.pcMemberName = this.$route.query.pcMemberName;
        this.reviewStatus = this.$route.query.reviewStatus;
        this.meetingStatus = this.$route.query.meetingStatus;
        this.meetingName = this.$route.query.meetingName;
        if(this.meetingStatus == "Reviewing" || this.meetingStatus == "ReviewCompleted") {
            this.reviewReadOnly = false;
            this.rebuttalHidden = true;
            this.rebuttalStatus = "beforeRebuttal";
            this.confirmHidden = true;
            this.discussionHidden = true;
          if(this.reviewStatus == "alreadyReviewed"){
            this.reviewReadOnly = true;
          }
        }else if(this.meetingStatus == "ResultPublished" || this.meetingStatus == "ReviewConfirmed") {
          this.submitButtonDisplay = "Update Review";
          this.reviewReadOnly = false;
          this.rebuttalHidden = true;
          this.rebuttalStatus = "beforeRebuttal";
          this.confirmHidden = false;
          this.discussionHidden = false;
          if(this.reviewStatus == "firstUpdated") {
            this.reviewReadOnly = true;
          } else if(this.reviewStatus == "reviewConfirmed") {
            this.reviewReadOnly = true;
            this.confirmHidden = true;
          }
        }else if(this.meetingStatus == "RebuttalFinish" || this.meetingStatus == "ReviewFinish") {
          this.submitButtonDisplay = "Update Review";
          this.reviewReadOnly = false;
          this.rebuttalHidden = false;
          this.rebuttalStatus = "afterRebuttal";
          this.confirmHidden = false;
          this.discussionHidden = false;
          if(this.reviewStatus == "secondUpdated") {
            this.reviewReadOnly = true;
          } else if(this.reviewStatus == "finalConfirmed") {
            this.reviewReadOnly = true;
            this.confirmHidden = true;
          }
        }else if(this.meetingStatus == "ReviewPublish") {
          this.submitButtonDisplay = "Update Review";
          this.reviewReadOnly = true;
          this.rebuttalHidden = false;
          this.rebuttalStatus = "afterRebuttal";
          this.confirmHidden = true;
          this.discussionHidden = false;
        }
        this.loadReviewInfo();
        this.loadRebuttal();
        this.articleLoadCompleted = true;
      },

      loadRebuttal:function() {
        this.articleId = this.$route.query.articleId;
        let that = this;
        let requestUrl = "api/meeting/rebuttalInfo";
        this.$axios.get(
          requestUrl,
          {params: {
              articleId: that.articleId,
            }}
        )
          .then(resp => {
            //console.log(resp)
            if(resp.data.responseCode == 200){
              if(resp.data.responseMessage == "success"){
              let rebuttal = resp.data.responseBody.rebuttal;
              that.rebuttal = rebuttal.content;
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
      loadReviewInfo:function() {
        let articleId = this.$route.query.articleId;
        let pcMemberName = this.$route.query.pcMemberName;
        let that = this;
        let requestUrl = "api/meeting/alreadyReviewedInfo";
        this.$axios.get(
          requestUrl,
          {params: {
              articleId: articleId,
              pcMemberName: pcMemberName
            }}
        )
          .then(resp => {
            //console.log(resp)
            if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
              let ref = resp.data.responseBody.alreadyReviewedInfo;
              if(ref.score != 0){
                that.comment = ref.reviews;
                that.confidenceRating["rating"] = that.reConfidenceSet[ref.confidence];
                that.markRating["rating"] = that.reMarkSet[ref.score];
              }
            }else{
              this.tips_text = "error occurred due to " + resp.data.responseBody.reason;
              this.$toast(this.tips_text,{color:'red'})
            }
            that.loadComplete = true;
          })
          .catch(error => {
            console.log(error);
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

<style scoped>
  #essayDetail{
    background-color: rgb(232, 232, 232) !important;
  }
</style>
