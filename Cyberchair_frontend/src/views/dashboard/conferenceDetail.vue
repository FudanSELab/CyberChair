<template>
  <v-container>
    <conferenceInfo
      ref="conferenceInfo"
      :displayOnly = "true"
      :conferenceInfo = "meetingInfo"
      v-if="infoUpdated"
    ></conferenceInfo>
    <v-col cols="10" class="lighten-5 px-10 mx-5 align-center">
      <v-divider></v-divider>
        <v-card class="mx-auto" max-width="100%" v-if="infoUpdated" style="background-color: rgb(232, 232, 232)">
            <v-card-text>
                <div>Meeting Status</div>
                <p class="display-1 text--primary">
                    {{this.meetingInfo.status}}
                </p>
            </v-card-text>
            <v-card-actions>
                <v-btn text v-if="meetingInfo.status=='ApplyPassed'" v-on:click="beginSubmission()">
                    Begin Submisson
                </v-btn>
                <v-btn text v-if="meetingInfo.status=='SubmissionAvailable'" v-on:click="beginReview()">
                    End Submisson & Begin Review
                </v-btn>
                <v-btn text v-if="meetingInfo.status=='SubmissionAvailable'||meetingInfo.status=='ApplyPassed'"
                    v-on:click="pcmInvite()">
                    Invite PCMembers
                </v-btn>
                <v-btn text v-if="meetingInfo.status=='ReviewCompleted'" v-on:click="beginDiscussion()">
                    End Review & Begin Discussion
                </v-btn>
                <v-btn text v-if="meetingInfo.status=='ReviewConfirmed'">
                    Preliminary Results Already Published
                </v-btn>
                <v-btn text v-if="meetingInfo.status=='RebuttalFnish'">
                    New Round Review in process
                </v-btn>
                <v-btn text v-if="meetingInfo.status=='ReviewFinish'" v-on:click="publishFinalResult()">
                    Publish Final Results
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-col>
    <v-dialog v-model="dialog" max-width="750" persistent>
        <v-card>
            <v-card-title class="headline">Begin Review Confirm</v-card-title>
            <v-card-text>Are you sure you will end the submission and begin the review? which review strategy do you want to adapt?</v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green darken-1" text v-on:click="review('TopicRelevant')">TopicRelevant</v-btn>
                <v-btn color="green darken-1" text v-on:click="review('LoadBalancing')">LoadBalancing</v-btn>
                <v-btn color="green darken-1" text v-on:click="dialog = false">Cancel</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import conferenceInfo from '../dashboard/cyberchairComponents/conferenceInfo'
export default {
    name: 'conferenceDetail',
    components:{
        conferenceInfo
    },
    data() {
        return {
            dialog: false,
            infoUpdated: false,
            meetingInfo: {
                meetingName: '',
                acronym: '',
                region: '',
                city: '',
                venue: '',
                organizer: '',
                webPage: '',
                deadlineMenu: false,
                acceptanceMenu: false,
                mainConferenceMenu: false,
                submissionDeadlineDate: new Date().toISOString().substr(0, 10),
                notificationOfAcceptanceDate : new Date().toISOString().substr(0, 10),
                conferenceDate : new Date().toISOString().substr(0, 10),
                topic: []
            },
        }
    },
    methods: {
        beginSubmission(){
            let requestUrl = 'api/meeting/beginSubmission'
            let meetingName = this.$route.query.meetingName
            this.$axios.post(
            requestUrl, {
                meetingName: meetingName
            }).then(resp =>{
                if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
                    this.tips_text = "Begin submission successfully"
                    this.$toast(this.tips_text, {color: 'green'})
                    window.setTimeout(function(){
                        this.$router.go()
                    }.bind(this), 1000)
                }else {
                    this.tips_text = "meetings loaded error due to" + resp.data.responseBody.reason;
                    this.$toast(this.tips_text, {color: 'red'})
                }
            }).catch(error => {
                this.tips_text = "network error";
                this.$toast(this.tips_text, {color: 'red'})
            })
        },
        pcmInvite(){
            let meetingName = this.$route.query.meetingName
            this.$router.push({
                path: '/pages/pcminvite',
                query: {
                    meetingName: meetingName
                }
            })
        },
        beginReview(){
            this.dialog = true
        },
        review(strategy){
            let requestUrl = 'api/meeting/beginReview'
            let meetingName = this.$route.query.meetingName
            this.$axios.post(
            requestUrl, {
                meetingName: meetingName,
                assignStrategy: strategy
            }).then(resp =>{
                if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
                    this.tips_text = "Begin Review successfully"
                    this.$toast(this.tips_text, {color: 'green'})
                    window.setTimeout(function(){
                        this.$router.go()
                    }.bind(this), 1000)
                }else {
                    this.tips_text = "meetings loaded error due to" + resp.data.responseBody.reason;
                    this.$toast(this.tips_text, {color: 'red'})
                }
            }).catch(error => {
                this.tips_text = "network error";
                this.$toast(this.tips_text, {color: 'red'})
            })
        },
        beginDiscussion(){
            let requestUrl = 'api/meeting/publish'
            let meetingName = this.$route.query.meetingName
            this.$axios.post(
            requestUrl, {
                meetingName: meetingName
            }).then(resp =>{
                if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
                    this.tips_text = "Results Publish successfully"
                    this.$toast(this.tips_text, {color: 'green'})
                    window.setTimeout(function(){
                        this.$router.go()
                    }.bind(this), 1000)
                }else {
                    this.tips_text = "Publish error due to" + resp.data.responseBody.reason;
                    this.$toast(this.tips_text, {color: 'red'})
                }
            }).catch(error => {
                this.tips_text = "network error";
                this.$toast(this.tips_text, {color: 'red'})
            })
        },
        publishFinalResult(){
            let requestUrl = 'api/meeting/finalPublish'
            let meetingName = this.$route.query.meetingName
            this.$axios.post(
            requestUrl, {
                meetingName: meetingName
            }).then(resp =>{
                if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
                    this.tips_text = "Final Results Publish successfully"
                    this.$toast(this.tips_text, {color: 'green'})
                    window.setTimeout(function(){
                        this.$router.go()
                    }.bind(this), 1000)
                }else {
                    this.tips_text = "Publish error due to" + resp.data.responseBody.reason;
                    this.$toast(this.tips_text, {color: 'red'})
                }
            }).catch(error => {
                this.tips_text = "network error";
                this.$toast(this.tips_text, {color: 'red'})
            })
        },
    },
    mounted: function(){
        let requestUrl = 'api/meeting/meetingInfo'
        let meetingName = this.$route.query.meetingName
        let that = this
        this.$axios.get(
            requestUrl, {
                params: {meetingName: meetingName}
            }
        ).then(resp =>{
            if (resp.data.responseCode == 200 && resp.data.responseMessage == "success") {
                let meetingInfo = resp.data.responseBody.meetingInfo
                that.meetingInfo = meetingInfo
                that.infoUpdated = true
            }else {
                this.tips_text = "meetings loaded error due to" + resp.data.responseBody.reason;
                this.$toast(this.tips_text, {color: 'red'})
            }
        }).catch(error => {
            this.tips_text = "network error";
            this.$toast(this.tips_text, {color: 'red'})
        })
    }
}
</script>
