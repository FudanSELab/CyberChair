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
        items: [],
        titleUnprocessed:"To ratify",
        titleProcessed:"Ratified",
        meetingsUnprocessed: [],
        meetingsProcessed: [],
        functionsUnprocessed:[],
        functionsProcessed:[],
        displaySetUnprocessed:['chairname','city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],
        displaySetProcessed:['chairname','city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate'],
        displayName:"meetingName",
        meetingType:{
          queueingApplication:"queueingApplication",
          alreadyApplication:"alreadyApplication",
        },
        meetingList:{
          queueingApplication:[],
          alreadyApplication:[],
        },
        displaySet: {
          queueingApplication:['acronym','chairName','city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate','topic'],
          alreadyApplication:['acronym','chairName','city','region','venue','primaryArea','secondaryArea','organiser','webPage','submissionDeadlineDate','conferenceDate','topic'],
        },
        title:{
          queueingApplication:"To Ratify",
          alreadyApplication:"Ratified",
        },
        functions:{
          queueingApplication:[],
          alreadyApplication:[],
        },

      }
    },
    methods: {
        loadMeetings(){
          let username = localStorage.username;
          let requestUrl = {
            queueingApplication:"api/admin/queueingApplication",
            alreadyApplication:"api/admin/alreadyApplication",
          };
          let that = this;
          for(let type in that.meetingType) {
            this.$axios.get(
              requestUrl[type], {
                params: {username: username}
              })
              .then(resp => {
                if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
                  let meetings = resp.data.responseBody[type];
                  for(let i = 0; i <  meetings.length; i++){
                    let meeting = meetings[i];
                    let topic = "";
                    for(let j = 0; j < meeting["topic"].length; j++) {
                      topic = topic + meeting["topic"][j] + "\n";
                    }
                    meeting["topic"] = topic;
                    console.log(meeting);
                    meeting["functions"] = [];
                    if(type == "queueingApplication")
                    {
                      meeting["functions"].push(that.newFunction("approve","Approve","green"));
                      meeting["functions"].push(that.newFunction("reject","Reject","red"));
                    }
                    else if(type == "alreadyApplication")
                    {
                      meeting["functions"].push(that.newFunction("ratified","Ratified","green"));
                    }
                    that.meetingList[type].push(meeting);
                  }

                } else {
                  this.tips_text = "meetings loaded error due to" + resp.data.responseBody.reason;
                  this.$toast(this.tips_text, {color: 'red'})
                }
              })
              .catch(error => {
                console.log(error);
                this.tips_text = "network error";
                this.$toast(this.tips_text, {color: 'red'})
              })
            console.log(that.meetingList);
          }

        },
      // loadMeetings(){
      //   var requestUrl = "/api/admin/queueingApplication";
      //   var that = this;
      //   this.$axios.get(requestUrl)
      //     .then(resp => {
      //       if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
      //          let meetings = resp.data.responseBody.meetings;
      //          that.meetingsUnprocessed.clear;
      //          that.meetingsProcessed.clear;
      //          for(let i = 0; i < meetings.length; i++) {
      //            let meeting = meetings[i];
      //            meeting["functions"] = [];
      //            switch (meeting.approval) {
      //              case "Unprocessed":
      //                meeting["functions"].push(this.newFunction("approve","Approve","green"));
      //                meeting["functions"].push(this.newFunction("reject","Reject","red"));
      //                that.meetingsUnprocessed.push(meeting);
      //                break;
      //              case "ApplyFailed":
      //                meeting["functions"].push(this.newFunction("rejected","Rejected","red"));
      //                that.meetingsProcessed.push(meeting);
      //                break;
      //              default:
      //                meeting["functions"].push(this.newFunction("approved","Approved","green"));
      //                that.meetingsProcessed.push(meeting);
      //                break;
      //            }
      //          }
      //       }else{
      //           this.tips_text = "meetings loaded error due to" + resp.data.responseBody.reason;
      //           this.$toast(this.tips_text,{color:'red'})
      //       }
      //     })
      //     .catch(error => {
      //         this.tips_text = "network error";
      //         this.$toast(this.tips_text,{color:'red'})
      //     })
      // },
      newFunction(functionName,componentName,displayColor) {
        let func = {
          "functionName":"default",
          "componentName":"default",
          "displayColor": "green"
        };
        func["functionName"] = functionName;
        func["componentName"] = componentName;
        func["displayColor"] = displayColor;
        return func;
      },
      loadingFunctions(){
        let that = this;
        this.functions["alreadyApplication"]["rejected"] = function (item) {
        };
        this.functions["alreadyApplication"]["approved"] = function (item) {
        };
        this.functions["queueingApplication"]["approve"] = function (item) {
          that.$axios.post('api/admin/ratify',
            {
              meetingName:item.meetingName,
              approvalStatus: "ApplyPassed"
            })
            .then(resp =>
            {
              if(resp.data.responseCode == 200 && (resp.data.responseMessage == "success"))
              {
                that.tips_text = "approve success";
                that.$toast(that.tips_text,{color:'green'})
                window.setTimeout(function(){
                  that.$router.go();
                }.bind(that), 1000);
              }
              else
              {
                that.tips_text = "approve error due to " + resp.data.responseBody.reason;
                that.$toast(that.tips_text,{color:'red'})
              }
            })
            .catch(error => {
              that.tips_text = "network error";
              that.$toast(that.tips_text,{color:'red'})
            })
        };
        this.functions["queueingApplication"]["reject"] = function (item) {
          that.$axios.post('/api/admin/ratify',
            {
              meetingName:item.meetingName,
              approvalStatus: "ApplyFailed"
            })
            .then(resp =>
            {
              if(resp.data.responseCode == 200 && (resp.data.responseMessage == "success"))
              {
                that.tips_text = "reject success";
                that.$toast(that.tips_text,{color:'green'})
                window.setTimeout(function(){
                  that.$router.go();
                }.bind(that), 1000);
              }
              else
              {
                that.tips_text = "approve error due to " + resp.data.responseBody.reason;
                that.$toast(that.tips_text,{color:'red'})
              }
            })
            .catch(error => {
              that.tips_text = "network error";
              that.$toast(that.tips_text,{color:'red'})
            })
        };
        // this.functionsProcessed["rejected"] = function (item) {
        // };
        // this.functionsProcessed["approved"] = function (item) {
        // };
        // this.functionsUnprocessed["approve"] = function (item) {
        //   that.$axios.post('/api/meeting/admin/ratify',
        //     {
        //       meetingid:item.id,
        //       status: "approved"
        //     })
        //     .then(resp =>
        //     {
        //       if(resp.data.responseCode == 200 && (resp.data.responseMessage == "success"))
        //       {
        //           that.tips_text = "approve success";
        //           that.$toast(that.tips_text,{color:'green'})
        //           window.setTimeout(function(){
        //             that.$router.go();
        //           }.bind(that), 1000);
        //       }
        //       else
        //       {
        //         that.tips_text = "approve error due to " + resp.data.responseBody.reason;
        //         that.$toast(that.tips_text,{color:'red'})
        //       }
        //     })
        //     .catch(error => {
        //       that.tips_text = "network error";
        //       that.$toast(that.tips_text,{color:'red'})
        //   })
        // };
        // this.functionsUnprocessed["reject"] = function (item) {
        //   that.$axios.post('/api/meeting/admin/ratify',
        //     {
        //       meetingid:item.id,
        //       status: "rejected"
        //     })
        //     .then(resp =>
        //     {
        //       if(resp.data.responseCode == 200 && (resp.data.responseMessage == "failed"))//backend modify later
        //       {
        //           that.tips_text = "reject success";
        //           that.$toast(that.tips_text,{color:'green'})
        //           window.setTimeout(function(){
        //             that.$router.go()
        //           }.bind(that), 1000);
        //       }
        //       else
        //       {
        //         that.tips_text = "reject error due to " + resp.data.responseBody.reason;
        //         that.$toast(that.tips_text,{color:'red'})
        //       }
        //     })
        //     .catch(error => {
        //       console.log(error)
        //       that.tips_text = "network error";
        //       that.$toast(that.tips_text,{color:'red'})
        //     })
        // };
      },

    },
    mounted: function(){
      this.loadingFunctions();
      this.loadMeetings();
    }

  }
</script>
