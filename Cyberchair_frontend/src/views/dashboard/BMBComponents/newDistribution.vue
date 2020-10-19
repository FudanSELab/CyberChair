<template>
<v-container>
    <v-list
      id="fileinfo"
      color="rgb(232, 232, 232)"
      class="lighten-5 px-10 mx-5 align-center"
    >

    <p
    class="display-1 font-weight-medium mt-2 pt-2 mb-1 pb-1"
    >新增涉密文件分发信息
    </p>

    <v-form ref="newDistributionForm">
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
                            v-model="filename"
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
                发送方式
                </p>

                <v-radio-group v-model="sendMethod" row :rules="[rules.required]">
                    <v-radio label="自送" value="0"></v-radio>
                    <v-radio label="机要交通" value="1"></v-radio>
                    <v-radio label="公务网" value="2"></v-radio>
                </v-radio-group>

                <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                部门
                </p>
                <v-row justify="space-around">
                    <v-checkbox v-model="sections" label="民政厅" value="1"></v-checkbox>
                    <v-checkbox v-model="sections" label="财政厅" value="2"></v-checkbox>
                    <v-checkbox v-model="sections" label="保密办" value="3"></v-checkbox>
                </v-row>

                <v-btn
                class="green lighten-1"
                v-on:click="submitNewDistribution()"
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
            sections: ['1', '2'],
            display: false,
            sendMethod:'',
            filename:'',
            rules: {
                required: value => !!value || 'This field is required.',
            }
        }
    },

    methods: {

        submitNewDistribution() {
            if(!this.$refs.newDistributionForm.validate()) return false
            let requestUrl = "http://localhost:8080/addDistribution"
            this.$axios.post(requestUrl, {
              documentName: this.filename,
              sendMethod: this.sendMethod,
              sections: this.secretLevel
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