<template>
    <v-container
    id="notification"
    fluid
    tag="section"
    >
    <v-row justify="center">
      <v-col cols="12" md="10">
        <base-material-card
          color="rgb(223,23,12)"
        >
            <template v-slot:heading>
                 <v-card-title class="display-2 font-weight-medium rgb(223,23,12)">
                    Your Notifications
                </v-card-title>

                <v-card-subtitle class="subtitle-1 font-weight-medium white--text rgb(223,23,12)">
                    Unhandled Notifications
                </v-card-subtitle>

            </template>
            <v-divider class="my-3 py-0"></v-divider>

            <v-app>
                <v-container>
                    <v-col
                        v-for="(item, i) in items"
                        :key="i"
                        cols="6"

                    >
                    <v-card
                        color="orange accent-4"
                        dark
                    >
                     <v-row class="d-flex align-center justify-space-around">
                        <v-col cols="6">
                            <v-card-title
                                class="subtitle"
                                v-text="item.meetingName"
                            ></v-card-title>

                            <v-card-subtitle v-text="'Chair: '+item.chairName" class="my-0"></v-card-subtitle>
                        </v-col>
                        <v-col cols="5">
                                <v-btn text v-on:click="accept(item)">ACCEPTED</v-btn>
                                <v-btn text v-on:click="refuse(item)">REJECTED</v-btn>
                        </v-col>

                    </v-row>
                    </v-card>


                    </v-col>
                    <div v-if="items.length==0">No data available</div>
                </v-container>
            </v-app>



        </base-material-card>
        <base-material-card
            color="rgb(223,23,12)"
            class="my-12"
        >
        <template v-slot:heading>
                <v-card-title class="display-2 font-weight-medium rgb(223,23,12)">
                    Handled Notifications
                </v-card-title>

            </template>
            <v-divider class="my-3 py-0"></v-divider>

            <v-app>
                <v-container>
                    <v-col
                        v-for="(item, i) in items_done"
                        :key="i"
                        cols="6"
                    >
                    <v-card
                        color="grey lighten-1"
                        dark
                    >
                    <v-row class="d-flex align-center justify-space-around">
                        <v-col cols="6">
                            <v-card-title
                                class="subtitle"
                                v-text="item.meetingName"
                            ></v-card-title>

                            <v-card-subtitle v-text="'Chair: '+item.chairName" class="my-0"></v-card-subtitle>
                        </v-col>
                        <v-col cols="5">
                                <v-btn text disabled v-if="item.choice=='accepted'">ACCEPTED</v-btn>
                                <v-btn text disabled v-if="item.choice=='rejected'">REJECTED</v-btn>
                        </v-col>

                    </v-row>
                    </v-card>
                    </v-col>
                    <div v-if="items_done.length==0">No data available</div>
                </v-container>
            </v-app>

            <v-dialog
                v-model="dialog"
                max-width="700"
                persistent
            >
                <v-card>
                    <v-card-title class="headline">Invitation Confirm</v-card-title>
                    <v-card-text>
                        Are you sure you will {{situation}} {{chairman}}'s invitation?
                        <p v-if="situation=='accepted'">Now select the topic you can be an reviewer:</p>
                        <topicOptions
                            v-if="situation=='accepted'"
                            :displayOnly="false"
                            :initTopics="selectedTopic"
                            :availableTopics="availableTopics"
                            :availableTopicsRevisable="false"
                            @topicChange="getTopics"
                        ></topicOptions>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn
                        color="green darken-1"
                        text
                        @click="dialog = false"
                        >
                        Cancel
                        </v-btn>
                        <v-btn
                        color="green darken-1"
                        text
                        @click="sendfeedback()"
                        >
                        {{situation}}
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </base-material-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import topicOptions from './cyberchairComponents/topicOptions'

    export default{
        name: 'notification',
        components:{
          topicOptions,
        },
        data() {
            return {
                    tips_text: '',
                    dialog:false,
                    chairman: '',
                    meetingName: '',
                    situation:'',
                    availableTopics:[],
                    selectedTopic: [],
                    items: [],
                    items_done: [],
            };
        },
        methods: {
            accept(item){
                this.chairman = item.chairName;
                this.meetingName = item.meetingName;
                this.selectedTopic = [...item.topic];
                this.availableTopics = [...item.topic];
                this.situation='accepted';
                this.dialog=true;
            },
            refuse(item){
                this.chairman = item.chairName;
                this.meetingName = item.meetingName;
                this.situation='rejected';
                this.dialog=true;
            },
            sendfeedback(){
                if(this.situation == 'accepted' && this.selectedTopic.length == 0){
                    return false;
                }
                this.$axios.post('api/user/invitationRepo',
                    {
                        username:localStorage.username,
                        meetingName:this.meetingName,
                        response:this.situation,
                        topic: this.selectedTopic
                    }
                ).then(resp =>{
                    if (resp.status === 200 && resp.data.responseMessage === 'success') {
                        this.$router.go()
                    }
                }).catch(error =>{
                    console.log(error);
                });
            },
            getTopics(val) {
                this.selectedTopic = val
            },
        },
        mounted: function(){
            var that = this;
            this.$axios.get('api/user/alreadyDealedNotifications',
                {params:{username: localStorage.username}}
            ).then(resp => {
                if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                    console.log(resp);
                    var invs = resp.data.responseBody.alreadyDealedNotifications;
                    for(var i=0;i<invs.length;i++){
                        if(invs[i]["chairName"] == localStorage.username)continue;
                        that.items_done.push({"meetingName":invs[i]["meetingName"],"chairName":invs[i]["chairName"],
                        "color":"#1F7087","choice":invs[i]["status"]})
                    }
                }else{
                    this.tips_text = "errors occurred when getting notification information";
                    this.$toast(this.tips_text,{color:'red'})
              }
            }).catch(error => {
                this.tips_text = "error occurred when loading notification data";
                this.$toast(this.tips_text,{color:'red'})
            });
            this.$axios.get('api/user/undealedNotifications',
                {params:{username: localStorage.username}}
            ).then(resp => {
                if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                    var invs = resp.data.responseBody.invitations;
                    for(var i=0;i<invs.length;i++){
                        that.items.push({"meetingName":invs[i]["meetingName"],"chairName":invs[i]["chairName"],
                        "topic":invs[i]["topic"], "color":"#1F7087"})
                    }
                }else{
                    this.tips_text = "errors occurred when getting notification information";
                    this.$toast(this.tips_text,{color:'red'})
                }
            }).catch(error => {
                this.tips_text = "error occurred when loading notification data";
                this.$toast(this.tips_text,{color:'red'})
          });
        }
    }
</script>
<style scoped>
.v-application >>> .v-application--wrap{min-height: auto;}
.cachedsss{
    color: rgb(23, 223, 12);
}
</style>
