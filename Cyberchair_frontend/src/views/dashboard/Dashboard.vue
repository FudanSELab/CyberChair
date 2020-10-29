<template>
  <v-container
    id="dashboard"
    fluid
  >
    <div
      v-for="item in meetingType"
      v-bind:key="item"
    >
      <display-list
        :_title="title[item]"
        :_display-set="displaySet[item]"
        :_display-name="displayName"
        :_functions="functions[item]"
        :_items="meetingList[item]"
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
        displayName:"meetingName",
        meetingType:{
          chairMeeting:"chairMeeting",
          pcMemberMeeting:"pcMemberMeeting",
          authorMeeting:"authorMeeting",
          availableMeeting:"availableMeeting"
        },
        meetingList:{
          chairMeeting:[],
          pcMemberMeeting:[],
          authorMeeting:[],
          availableMeeting:[]
        },
        displaySet: {
          chairMeeting: ["acronym", "conferenceDate", "topic"],
          pcMemberMeeting: [ "acronym", "conferenceDate", "topic"],
          authorMeeting: [ "acronym", "submissionDeadlineDate", "topic"],
          availableMeeting: [ "acronym", "submissionDeadlineDate", "topic"],
        },
        title:{
          chairMeeting:"Role As Chair",
          pcMemberMeeting:"Role As PCMember",
          authorMeeting:"Role As Author",
          availableMeeting:"All Available Meeting List"
        },
        functionParam:{
          chairMeeting:{
            functionName:"checkDetail",
            display:"Check Detail",
            color:"green"
          },
          pcMemberMeeting:{
            functionName:"getReviewList",
            display:"Check ReviewList",
            color:"green"
          },
          authorMeeting:{
            functionName:"checkSubmission",
            display:"Check Submission",
            color:"green"
          },
          availableMeeting:{
            functionName:"articleSubmit",
            display:"Submit Article",
            color:"green"
          }
        },
        functions:{
          chairMeeting:[],
          pcMemberMeeting:[],
          authorMeeting:[],
          availableMeeting:[]
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
      loadRelatedMeetings(){
        let username = localStorage.username;
        let requestUrl = {
          chairMeeting:"api/user/chairMeeting",
          pcMemberMeeting: "api/user/pcMemberMeeting",
          authorMeeting:"api/user/authorMeeting",
          availableMeeting:"api/user/availableMeeting"
        };
        let that = this;
        for(let type in that.meetingType) {
          this.$axios.get(
            requestUrl[type], {
              params: {username: username}
            })
            .then(resp => {
              if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
                  let meetings = resp.data.responseBody.meetings;
                  let param = that.functionParam[type];
                  // console.log(type + "markmarkmark!");
                  // console.log(meetings);
                  for(let i = 0; i <  meetings.length; i++){
                    let meeting = meetings[i];
                    console.log(meeting);
                    let topic = "";
                    for(let j = 0; j < meeting["topic"].length; j++) {
                      topic = topic + meeting["topic"][j] + "\n";
                    }
                    meeting["topic"] = topic;
                    meeting["functions"] = [];
                    meeting["functions"].push(that.newFunction(param.functionName,param.display,param.color));
                    that.meetingList[type].push(meeting);
                  }

              } else {
                this.tips_text = "meetings loaded error due to" + resp.data.responseBody.reason;
                this.$toast(this.tips_text, {color: 'red'})
              }
            })
            .catch(error => {
              this.tips_text = "network error";
              this.$toast(this.tips_text, {color: 'red'})
            })
        }
        // for(let type in that.meetingType){
        //   console.log(type + that.meetingList[type]);
        // }
      },
      // loadAllMeetings(){
      //   var requestUrl = "/api/user/meetinglist";
      //   var that = this;
      //   this.$axios.get(requestUrl)
      //     .then(resp => {
      //       if(resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
      //         let meetings = resp.data.responseBody.meetings;
      //         for(let i = 0; i < meetings.length; i++)
      //         {
      //           let meeting = meetings[i];
      //           meeting["functions"] = [];
      //           if(meeting.approval ==  "ApplyPassed") {
      //             console.log("applyPassed");
      //             meeting["functions"].push(this.newFunction("submitEssayDisabled", "Submit Essay", "grey"));
      //           }
      //           else if (meeting.approval ==  "SubmittedAvailable")
      //           {
      //             console.log("SubmittedAvailable");
      //             meeting["functions"].push(this.newFunction("submitEssay", "Submit Essay","green"));
      //           }
      //           if(that.meetingsAsChair.filter(e => e.id == meeting.id).length > 0){continue;}
      //           if(that.meetingsAsAuthor.filter(e => e.id == meeting.id).length > 0){continue;}
      //           that.otherMeetings.push(meeting);
      //         }
      //       }
      //       else{
      //         this.tips_text = "meetings loaded error due to" + resp.data.responseBody.reason;
      //         this.$toast(this.tips_text,{color:'red'})
      //       }
      //     })
      //     .catch(error => {
      //       this.tips_text = "network error";
      //       this.$toast(this.tips_text,{color:'red'})
      //     })
      // },
      loadFunctions()
      {
        let that = this;
        this.functions["chairMeeting"]["checkDetail"] = function(item){
          let username = localStorage.username;
          that.$router.push({
            path: '/pages/chairMeeting',
            query: {
              chairName:username,
              meetingName:item.meetingName
            }
          })
        };
        this.functions["pcMemberMeeting"]["getReviewList"] = function(item){
          let username = localStorage.username;
          that.$router.push({
            path: '/meeting/reviewArticles',
            query: {
              pcMemberName:username,
              meetingName:item.meetingName
            }
          })
        };
        this.functions["authorMeeting"]["checkSubmission"] = function(item){
          let username = localStorage.username;
          that.$router.push({
            path: '/submission/list',
            query: {
              authorName:username,
              meetingName:item.meetingName
            }
          })
        };
        this.functions["availableMeeting"]["articleSubmit"] = function(item){
          let username = localStorage.username;
          that.$router.push({
            path: 'essaySubmission',
            query: {
              meetingName:item.meetingName,
              username:username
            }
          })
        };
        // this.chairFunctions["rejected"] = function (item) {
        // };
        // this.chairFunctions["querying"] = function (item) {
        // };
        // this.chairFunctions["invitePCMember"] = function (item) {
        //   that.$router.push({
        //     path: 'pages/pcminvite',
        //     query: {
        //       meetingid:item.id,
        //     }
        //   })
        // };
        // this.chairFunctions["beginSubmission"] = function (item) {
        //   //console.log(item.id)
        //   that.$axios.post('/api/meeting/submission',
        //     {"meetingid":String(item["id"])}
        //   ).then(resp => {
        //     if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
        //       that.tips_text = 'begin submission successfully';
        //       that.$toast(that.tips_text,{color:'green'})
        //       window.setTimeout(function(){
        //         that.$router.go()
        //       }.bind(that), 1000)
        //     }else{
        //       that.tips_text = "meetings loaded error due to network problem";
        //       that.$toast(that.tips_text,{color:'red'})
        //   }
        //   }).catch(error =>{
        //     that.tips_text = "network error";
        //     that.$toast(that.tips_text,{color:'red'})
        //   });
        // };
        // this.authorFunctions["submitEssay"] = function (item) {
        //   let username = localStorage.username;
        //   that.$router.push({
        //     path: 'essaySubmission',
        //     query: {
        //       meetingid:item.id,
        //       username:username
        //     }
        //   })
        // };
        // this.authorFunctions["checkSubmissions"] = function (item) {
        //   let username = localStorage.username;
        //   that.$router.push({
        //     path: '/submission/list',
        //     query: {
        //       username:username,
        //       meetingid:item.id
        //     }
        //   })
        // };
        // this.otherFunctions["submitEssay"] = function (item) {
        //   let username = localStorage.username;
        //   that.$router.push({
        //     path: 'essaySubmission',
        //     query: {
        //       meetingid:item.id,
        //       username:username
        //     }
        //   })
        // };
        // this.otherFunctions["submitEssayDisable"] = function (item) {
        // };
      },
      //wrong modify later
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
      //   this.chairDisplayList = this.listGenerator("Role: Chair",this.meetingsAsChair,['city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],this.chairFunctions);
      //   this.PCMemberDisplayList = this.listGenerator("Role: PC Member",this.meetingsAsPCMember,['city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],this.PCMemberFunctions);
      //   this.authorDisplayList = this.listGenerator("Role: Author",this.meetingsAsAuthor,['city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],this.authorFunctions);
      //   this.otherDisplayList = this.listGenerator("Other Meetings",this.otherMeetings,['city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],this.otherFunctions);
      //   },
    },

    mounted: function(){
      this.loadFunctions();
      this.loadRelatedMeetings();
      // this.loadAllMeetings();
      // this.loadDisplayLists();
    }

  }
</script>

<style scoped>
.red_title{
  background-color: rgb(223,23,12);
}
</style>
