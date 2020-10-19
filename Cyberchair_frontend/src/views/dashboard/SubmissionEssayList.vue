<template>
  <v-container fluid>
      <display-list
        :_title="title"
        :_display-set="displaySet"
        :_display-name="displayName"
        :_functions="functions"
        :_items="articleList"
      >
      </display-list>
  </v-container>
</template>
<script>
import DisplayList from "./cyberchairComponents/displayList";
export default {
  components: {DisplayList},
  data() {
        return {
            meetingStatus:"",
            title:"Submissions",
            displaySet:["topic"],
            displayName:"title",
            functions:[],
            functionParam:{
              SubmissionAvailable:{
                functionName:"update",
                display:"Update",
                color:"green"
              },
              Reviewing:{
                functionName:"reviewing",
                display:"Reviewing",
                color:"green"
              },
              ResultPublished:{
                functionName:"checkReviews",
                display:"Check Reviews",
                color:"green"
              },
              accepted:{
                functionName:"accepted",
                display:"accepted",
                color:"green"
              },
              rejected:{
                functionName:"rejected",
                display:"rejected",
                color:"red"
              }
            },
            articleList:[],
            snackbar:false,
            snackbar_color:'success',
            timeout: 2000,
            tips_text: '',
            meetingName:'',
            authorName: '',
            list: undefined,
            status2color:{
                "accepted": "green",
                "rejected": "red",
                "queuing": "orange"
            }

        }


    },

    methods: {
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

        loadMeetingStatus:function() {
          let meetingName = this.$route.query.meetingName;
          this.meetingName = meetingName;
          this.authorName = this.$route.query.authorName;
          let url = "api/meeting/meetingInfo";
          let that = this;
          this.$axios.get(
            url,
            {params: {
                meetingName: meetingName
              }
            }
          )
            .then(resp => {
              if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                that.meetingStatus = resp.data.responseBody.meetingInfo.status;
                that.loadArticles();
                that.loadFunctions();
              }
              else{
                this.tips_text = 'error occurred, ' + resp;
                this.tips_text = 'error occurred, ' + resp;
                this.snackbar_color = 'error';
                this.snackbar = true;
              }

            })
            .catch(err=>{
              this.tips_text = 'error occurred when loading data from the server side';
              this.snackbar_color = 'error';
              this.snackbar = true;
            })
        },
        loadFunctions:function(){
          let that = this;
          let meetingName = this.$route.query.meetingName;
          this.functions["update"] = function(item){

            that.$router.push({
              path: '/essaySubmission',
              query: {
                meetingName:meetingName,
                authorName:that.authorName,
                articleId:item.id
              }
            })
          };
          this.functions["accepted"] = function(item){
          };
          this.functions["rejected"] = function(item){
          };
          this.functions["reviewing"] = function(item){
          };
          this.functions["checkReviews"] = function(item){
            that.$router.push({
              path: '/reviewDetail',
              query: {
                articleId:item.id,
                meetingStatus: that.meetingStatus,
                authorName:that.authorName,
                meetingName:that.meetingName
              }
            })
          };
        },

        loadArticles: function() {
            this.meetingName = this.$route.query.meetingName;
            this.authorName = this.$route.query.authorName;
            let meetingName = this.meetingName;
            let authorName = this.authorName;

            let that = this;
            let meetingStatus = this.meetingStatus;
            let URL_getSubmissions = "api/meeting/submissionList";
            let param = this.functionParam[meetingStatus];
            let resultPublishedFlag = false;
            if(this.meetingStatus == "SubmissionAvailable"){
                param = this.functionParam["SubmissionAvailable"];
            }else if(this.meetingStatus == "Reviewing" || this.meetingStatus == "ReviewCompleted" || this.meetingStatus == "ResultPublished"){
                param = this.functionParam["Reviewing"];
            }else{
              param = this.functionParam["ResultPublished"];
              resultPublishedFlag = true;

            }
            console.log("param:");
            console.log(param);
            this.$axios.get(
                URL_getSubmissions,
                {params: {
                    authorName: authorName,
                    meetingName: meetingName
                  }
                }
            )
            .then(resp => {
                if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                    let articles = resp.data.responseBody.articles;
                    for(let i = 0; i < articles.length; i++)
                    {
                      let article = articles[i];
                      let topic = "";
                      for(let j = 0; j < article["topic"].length; j++) {
                        topic = topic + article["topic"][j] + "\n";
                      }
                      article["topic"] = topic;
                      article["functions"] = [];
                      if(resultPublishedFlag){
                        let param2 = that.functionParam[article["status"]];
                        article["functions"].push(that.newFunction(param2.functionName,param2.display,param2.color));
                      }
                      article["functions"].push(that.newFunction(param.functionName,param.display,param.color));
                      console.log(article);
                      that.articleList.push(article);
                    }
                }
                else{
                    this.tips_text = 'error occurred, ' + resp;
                    this.snackbar_color = 'error';
                    this.snackbar = true;
                }

            })
            .catch(err=>{
                this.tips_text = 'error occurred when loading data from the server side';
                this.snackbar_color = 'error';
                this.snackbar = true;
            })

        },

        jumpToDetail: function(essayId){
            var url = "/essaydetail?essayid="+essayId;
            this.$router.replace(url)
        }

    },

  created: function() {
    this.loadMeetingStatus();
  }
};
</script>

<style scoped>
#wrapperList {
  background-color: rgb(238, 238, 238) !important;
}

.titleDisp {
  /* background-color: rgb(228, 228, 228); */
  /* color: rgba(121, 121, 121, 1); */
  height: 150px;
  line-height: 1.2em;
}

.trans {
  opacity: 0.92;
}
</style>
