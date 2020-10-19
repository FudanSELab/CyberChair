<template>
<v-container>
    <v-list
      id="fileinfo"
      color="rgb(232, 232, 232)"
      class="lighten-5 px-10 mx-5 align-center"
    >

    <p
    class="display-1 font-weight-medium mt-2 pt-2 mb-1 pb-1"
    >新增涉密文件信息
    </p>

    <v-form ref="newInfoForm">
        <v-col cols="8" class="mx-0 py-0">
            <v-list
            color="rgb(232, 232, 232)"
            class="lighten-5 px-10 mx-5 align-center"
            style="background-color: rgb(232, 232, 232)">
            <v-container>
                 <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                涉密文件名称
                </p>
                <v-row>
                    <v-col>
                        <v-text-field
                            v-model="fileinfo.filename"
                            type="text"
                            label="此处填写名称"
                            hint=""
                            :rules="[rules.required]"
                            :validate-on-blur="true"
                            :__disabled="display"
                        >
                        </v-text-field>
                    </v-col>
                </v-row>

                <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                发布日期
                </p>
                <v-date-picker
                    v-model="issueDate"
                    locale='zh-CN'
                    :rules="[rules.required]"
                >
                </v-date-picker>

                <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                保密等级
                </p>

                <v-radio-group v-model="secretLevel" row :rules="[rules.required]">
                    <v-radio label="内部" value="0"></v-radio>
                    <v-radio label="秘密" value="1"></v-radio>
                    <v-radio label="机密" value="2"></v-radio>
                    <v-radio label="绝密" value="3"></v-radio>
                </v-radio-group>

                <v-btn
                class="green lighten-1"
                v-on:click="submitNewSecretInfo()"
                >提交</v-btn>
            </v-container>

            </v-list>
        </v-col>
    </v-form>

    </v-list>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            fileinfo:{
                filename: '',
            },
            issueDate: '',
            secretLevel: '',
            display: false,
            rules: {
                required: value => !!value || 'This field is required.',
            }
        }
    },

    methods: {
        log() {
            console.log(this.issueDate)
        },

        submitNewSecretInfo() {
            if(!this.$refs.newInfoForm.validate()) return false
            let requestUrl = "http://localhost:8080/newInfo"
            this.$axios.post(requestUrl, {
              documentName: this.fileinfo.filename,
              date: this.issueDate,
              classfiedLevel: this.secretLevel
            }).then(resp => {
                if(resp.status == 200){
                    this.$toast('提交成功',{color:'green'})
                }else{
                    this.$toast('提交失败，请刷新后重新尝试',{color:'red'})
                }
                
            }).catch(error => {
                this.$toast('提交失败，请刷新后重新尝试',{color:'red'})
            })
        }
    }
}
</script>