<template>
  <v-container
    id="dashboard"
    fluid
  >
    <div
      v-for="item in articleType"
      v-bind:key="item"
    >
      <display-list
        :_title="title[item]"
        :_display-set="displaySet[item]"
        :_display-name="displayName"
        :_functions="functions[item]"
        :_items="articleList[item]"
      >
      </display-list>
    </div>
  </v-container>
</template>

<script>
  import displayList from "./cyberchairComponents/displayList"
  export default {
    components:{
      displayList
    },
    data () {
      return {
        tips_text: '',
        meetingStatus:"",
        meetingName:"",
        displayColorSet:[
          {"default":"green"},
          {"approved":"green"},
          {"rejected" : "red"},
          {"querying" : "orange"},
          {"Unprocessed":"orange"},
        ],
        state2Color:{
          Unprocessed:"info",
          applyFailed:"warning",
          applyPassed:"primary"
        },
        displayName:"title",
        articleType:{
          queuingArticle:"queuingArticle",
          alreadyReviewedArticle:"alreadyReviewedArticle"
        },
        articleList:{
          queuingArticle:[],
          alreadyReviewedArticle:[]
        },
        displaySet: {
          queuingArticle:["topic"],
          alreadyReviewedArticle:["topic"],
        },
        title:{
          queuingArticle:"To review",
          alreadyReviewedArticle:"Reviewed"
        },
        functions:{
          queuingArticle:[],
          alreadyReviewedArticle:[]
        }
      }
    },
    methods: {
      loadMeetingStatus:function() {
        let meetingName =  this.meetingName = this.$route.query.meetingName;
        let requestUrl = "api/meeting/meetingInfo";

        let that = this;
        this.$axios.get(
          requestUrl, {
            params: {
              meetingName: meetingName
            }
          })
          .then(resp => {
            if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
              let meetingInfo = resp.data.responseBody.meetingInfo;
              that.meetingStatus = meetingInfo.status;
            } else {
              this.tips_text = "meeting information load error due to" + resp.data.responseBody.reason;
              this.$toast(this.tips_text, {color: 'red'})
            }
          })
          .catch(error => {
            this.tips_text = "network error";
            this.$toast(this.tips_text, {color: 'red'})
          })
      },
      newFunction(functionName,componentName,displayColor) {
        let func = {
          "functionName":"default",
          "componentName":"default",
          "displayColor":"green"
        };
        func["functionName"] = functionName;
        func["componentName"] = componentName;
        func["displayColor"] = displayColor;
        return func;
      },
      loadArticles(){
        let pcMemberName = this.$route.query.pcMemberName;
        let meetingName =  this.$route.query.meetingName;
        let requestUrl = "api/meeting/reviewArticles";

        let that = this;
          this.$axios.get(
            requestUrl, {
              params: {
                pcMemberName: pcMemberName,
                meetingName: meetingName
              }
            })
            .then(resp => {
              if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
                let articles = resp.data.responseBody.reviewArticles;
                for(let i = 0; i <  articles.length; i++){
                  let article = articles[i];
                  article["functions"] = [];
                  let topic = "";
                  for(let j = 0; j < article["topic"].length; j++) {
                    topic = topic + article["topic"][j] + "\n";
                  }
                  article["topic"] = topic;
                  if(article["reviewStatus"] == "unReviewed")
                  {
                    article["functions"].push(that.newFunction("reviewArticle","Review","blue"));
                    that.articleList["queuingArticle"].push(article);
                  }
                  else
                  {
                    article["functions"].push(that.newFunction("alreadyReviewed","Reviewed","green"));
                    that.articleList["alreadyReviewedArticle"].push(article);

                  }
                }
              } else {
                this.tips_text = "articles loaded error due to" + resp.data.responseBody.reason;
                this.$toast(this.tips_text, {color: 'red'})
              }
            })
            .catch(error => {
              this.tips_text = "network error";
              this.$toast(this.tips_text, {color: 'red'})
            })

      },
      loadFunctions()
      {
        let that = this;
        this.functions["queuingArticle"]["reviewArticle"] = function(item){
          let username = localStorage.username;
          that.$router.push({
            path: '/meeting/reviewArticle',
            query: {
              pcMemberName:username,
              articleId:item.articleId,
              meetingName: that.meetingName,
              meetingStatus: that.meetingStatus,
              reviewStatus:item.reviewStatus
            }
          })
        };
        this.functions["alreadyReviewedArticle"]["alreadyReviewed"] = function(item){
          let username = localStorage.username;
          that.$router.push({
            path: '/meeting/reviewArticle',
            query: {
              pcMemberName:username,
              articleId:item.articleId,
              meetingName:that.meetingName,
              meetingStatus: that.meetingStatus,
              reviewStatus:item.reviewStatus
            }
          })
        };
      },
      listGenerator(title,items,displaySet,functions)
      {
        let list = {
          "title":"title",
          "items":[],
          "displaySet":[],
          "functions":[]
        };
        list["title"] = title;
        list["items"] = items;
        list["displaySet"] = displaySet;
        list["functions"] = functions;
        return list;
      },
      // loadDisplayLists() {
      //   this.chairDisplayList = this.listGenerator("Role: Chair",this.articlesAsChair,['city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],this.chairFunctions);
      //   this.PCMemberDisplayList = this.listGenerator("Role: PC Member",this.articlesAsPCMember,['city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],this.PCMemberFunctions);
      //   this.authorDisplayList = this.listGenerator("Role: Author",this.articlesAsAuthor,['city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],this.authorFunctions);
      //   this.otherDisplayList = this.listGenerator("Other articles",this.otherarticles,['city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],this.otherFunctions);
      //   },
    },

    created: function(){
      this.loadFunctions();
      this.loadArticles();
      this.loadMeetingStatus();
    }

  }
</script>

<style scoped>
  .red_title{
    background-color: rgb(223,23,12);
  }
</style>
