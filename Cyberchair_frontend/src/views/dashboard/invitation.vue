<template>
  <v-container id="invitation" fluid tag="section">
    <v-row justify="center">
      <v-col cols="12" md="10">
        <base-material-card color="rgb(223,23,12)">
          <template v-slot:heading>
            <v-card-title class="display-2 font-weight-medium rgb(223,23,12)">Invite Your PC member</v-card-title>

            <v-card-subtitle
              class="subtitle-1 font-weight-medium white--text rgb(223,23,12)"
            >You can search by fullname, username, institution.</v-card-subtitle>
          </template>
          <v-divider class="my-3 py-0"></v-divider>
          <v-list class="white lighten-5 px-4 py-1 mx-1">
            <v-app id="inspire">
              <v-container>
                <v-data-iterator
                  :items="items"
                  :items-per-page.sync="itemsPerPage"
                  :page="page"
                  :search="search"
                  :sort-by="sortBy.toLowerCase()"
                  :sort-desc="sortDesc"
                  hide-default-footer
                >
                  <template v-slot:header>
                    <v-row class="my-0 py-0">
                      <v-col cols="6"></v-col>
                      <v-col cols="3">
                        <v-text-field
                          v-model="search"
                          clearable
                          flat
                          solo-inverted
                          hide-details
                          prepend-inner-icon="search"
                          label="Search"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="3" class="px-0">
                        <template v-if="$vuetify.breakpoint.mdAndUp">
                          
                          <v-select
                            v-model="sortBy"
                            flat
                            solo-inverted
                            hide-details
                            :items="keys"
                            prepend-inner-icon
                            label="Sort by"
                          ></v-select>
                          <v-spacer></v-spacer>
                          <v-btn-toggle v-model="sortDesc" mandatory></v-btn-toggle>
                        </template>
                      </v-col>
                  
                    </v-row>
                  </template>

                  <template v-slot:default="props">
                    <v-row class="my-0 py-0">
                      <v-col
                        v-for="item in props.items"
                        :key="item.fullname"
                        cols="12"
                        sm="6"
                        md="6"
                        lg="4"
                      >
                        <v-card>
                          <v-card-title class="subheading font-weight-bold">{{ item.fullname }}</v-card-title>

                          <v-divider></v-divider>

                          <v-list dense>
                            <v-list-item
                              v-for="(key, index) in filteredKeys"
                              :key="index"
                              class="py-0 my-0"
                            >
                              <v-list-item-content
                                :class="{ 'red--text': sortBy === key }"
                              >{{ key }}</v-list-item-content>
                              <v-list-item-content
                                class="align-end"
                                :class="{ 'red--text': sortBy === key }"
                              >{{ item[key.toLowerCase()] }}</v-list-item-content>
                            </v-list-item>
                          </v-list>

                          <v-btn
                            class="green"
                            style="width:100%;margin-bottom:0px;"
                            v-on:click="pcmInvite(item)"
                          >Invite him/her</v-btn>
                        </v-card>
                      </v-col>
                    </v-row>
                  </template>

                  <template v-slot:footer>
                    <v-row class="mt-2" align="center" justify="center">
                      

                      <v-spacer></v-spacer>

                      <span class="mr-4 grey--text">Page {{ page }} of {{ numberOfPages }}</span>
                      <v-btn fab dark color="grey lighten-2" class="mr-1" @click="formerPage">
                        <v-icon>mdi-chevron-left</v-icon>
                      </v-btn>
                      <v-btn fab dark color="grey lighten-2" class="ml-1" @click="nextPage">
                        <v-icon>mdi-chevron-right</v-icon>
                      </v-btn>
                    </v-row>
                  </template>
                </v-data-iterator>
              </v-container>
            </v-app>
          </v-list>
        </base-material-card>

        <base-material-card color="rgb(223,23,12)" class="my-12">
          <template v-slot:heading>
            <v-card-title
              class="display-2 font-weight-medium rgb(223,23,12)"
            >People you have invited for this conference</v-card-title>
          </template>
          <v-divider class="my-3 py-0"></v-divider>
          <v-list>
            <v-app>
              <v-container>
                <v-row>
                  <v-col
                    v-for="item in items_inv"
                    :key="item.fullname"
                    cols="12"
                    sm="6"
                    md="6"
                    lg="4"
                  >
                    <v-card>
                      <v-card-title class="subheading font-weight-bold">{{ item.fullname }}</v-card-title>

                      <v-divider></v-divider>

                      <v-list dense>
                        <v-list-item v-for="(key, index) in filteredKeys" :key="index">
                          <v-list-item-content>{{ key }}:</v-list-item-content>
                          <v-list-item-content class="align-end">{{ item[key.toLowerCase()] }}</v-list-item-content>
                        </v-list-item>
                      </v-list>

                      <v-btn
                        :class="status2color[item.status]"
                        style="width:100%;margin-bottom:0px;"
                      >{{item.status}}</v-btn>
                    </v-card>
                  </v-col>
                </v-row>
              </v-container>
            </v-app>
            <v-dialog v-model="dialog" max-width="290" persistent>
              <v-card>
                <v-card-title class="headline">Invitation Confirm</v-card-title>
                <v-card-text>Are you sure you will invite {{invuser.username}} as you PC member?</v-card-text>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="green darken-1" text @click="dialog = false">Cancel</v-btn>
                  <v-btn color="green darken-1" text @click="pcmInviteReq()">Confirm</v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-list>
        </base-material-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
    export default{
        name: 'pcminvite',
        data() {
            return {
                tips_text: '',
                dialog: false,
                invuser: '',
                flag: false,
                itemsPerPageArray: [4, 8, 12],
                search: '',
                filter: {},
                sortDesc: false,
                page: 1,
                itemsPerPage: 4,
                sortBy: 'fullname',
                keys: [
                    'fullname',
                    'username',
                    'institution',
                    'email',
                ],
                items: [],
                items_inv: [],
                token2status:{
                    "undealed":"PENDING",
                    "accepted":"ACCEPTED",
                    "rejected":"REFUSED"
                },
                status2color:{
                    "ACCEPTED":"green",
                    "REFUSED":"red",
                    "PENDING":"orange"
                },
                app_ids:[],
            };
        },
        computed: {
            numberOfPages () {
                return Math.ceil(this.items.length / this.itemsPerPage)
            },
            filteredKeys () {
                return this.keys.filter(key => key !== `Name`)
            },
        },
        methods: {
            nextPage () {
                if (this.page + 1 <= this.numberOfPages) this.page += 1
            },
            formerPage () {
                if (this.page - 1 >= 1) this.page -= 1
            },
            updateItemsPerPage (number) {
                this.itemsPerPage = number
            },
            pcmInvite(item){
                this.flag=false;
                this.invuser=item;
                this.dialog=true;
            },
            pcmInviteReq(){
                // console.log(this.$route.query.meetingid);
                // console.log(this.invuser["id"])
                this.$axios.post('api/meeting/pcmInvitation',
                    {
                        meetingName: this.$route.query.meetingName,
                        pcMemberName: this.invuser["username"],
                    }
                ).then(resp => {
                    console.log(resp)
                    // 根据后端的返回数据修改
                        if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                            this.tips_text = 'successful application';
                            this.$toast(this.tips_text,{color:'green'})
                            window.setTimeout(function(){
                                this.$router.go();
                            }.bind(this), 1000);
                        } else {
                            this.tips_text = 'invite error ' + resp.data.responseBody.reason;
                            this.$toast(this.tips_text,{color:'red'})
                        }
                    })
                    .catch(error => {
                        this.tips_text = "network error";
                        this.$toast(this.tips_text,{color:'red'})
                    })
            },
        },
        mounted: function(){
            var that = this;
            var meetingName = this.$route.query.meetingName;
            this.$axios.get('api/meeting/invitationStatus',
                {params:{meetingName: meetingName}}
            ).then(resp => {            
                if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                    var users = resp.data.responseBody.invitationStatus;
                    for(var i=0;i<users.length;i++){
                        if(users[i]["status"] != 'rejected'){that.app_ids.push(users[i]["username"])}
                        that.items_inv.push({username:users[i]["username"],fullname:users[i]["fullname"],
                            institution:users[i]["institution"],
                            email:users[i]["email"],
                            status:this.token2status[users[i]["status"]]
                        });
                    }
                }else{
                    this.tips_text = "errors occurred when getting users information";
                    this.$toast(this.tips_text,{color:'red'})
                }
            }).catch(error => {
                this.tips_text = "error occurred when loading user data";
                this.$toast(this.tips_text,{color:'red'})
            });
            // console.log(that.app_ids)
            this.$axios.get('api/util/users',
                {params:{fullname:''}}
            ).then(resp => {            
                if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                    var users = resp.data.responseBody.users;
                    for(var i=0;i<users.length;i++){
                        if(users[i]["username"]=="admin")continue;
                        if(that.app_ids.includes(users[i]["username"]))continue;
                        if(users[i]["username"]==localStorage.username)continue;
                        that.items.push({username:users[i]["username"],fullname:users[i]["fullname"],
                        institution:users[i]["institution"],email:users[i]["email"]});
                    }
                }else{
                    this.tips_text = "errors occurred when getting users information";
                    this.$toast(this.tips_text,{color:'red'})
                }
            }).catch(error => {
                this.tips_text = "error occurred when loading user data";
                this.$toast(this.tips_text,{color:'red'})
          });
        }
    }

</script>
<style scoped>
.v-application >>> .v-application--wrap {
  min-height: auto;
}
</style>