package com.example.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_term2 : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)


    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_term2,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_setting()
            val bundle = Bundle()
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        var text = view.findViewById<TextView>(R.id.text)
        text.text = "개인정보 처리 방침 최종 개정 : 2021년 11월 25일\n" +
                "\n" +
                "핀더가든은(는) 개인정보 보호법 제30조에 따라 정보주체(고객)의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리지침을 수립·공개합니다.\n" +
                "\n" +
                "제 1조 (개인정보의 처리목적)\n" +
                "핀더가든은(는) 다음의 목적을 위하여 개인정보를 처리하고 있으며, 다음의 목적 이외의 용도로는이용하지 않습니다.\n" +
                "- 고객 가입의사 확인, 고객에 대한 서비스 제공에 따른 본인 식별.인증, 회원자격 유지.관리 \n" +
                "\n" +
                "제2조 (개인정보의 처리 및 보유기간)\n" +
                "1) 핀더가든은(는) 정보주체로부터 개인정보를 수집할 때 동의받은 개인정보 보유.이용기간 또는 법령에 따른 개인정 보 보유.이용기간 내에서 개인정보를 처리·보유합니다.\n" +
                "2) 구체적인 개인정보 처리 및 보유 기간은 다음과 같습니다.\n" +
                "- 고객 가입 및 관리 : 서비스 이용계약 또는 회원가입 해지시까지, 다만 채권·채무관계 잔존시에는 해당 채권·채 무관계 정산시까지\n" +
                "- 전자상거래에서의 계약·청약철회, 대금결제, 재화 등 공급기록 : 5년 \n" +
                "\n" +
                "제3조 (개인정보의 제3자 제공)\n" +
                "핀더가든은(는) 정보주체의 별도 동의, 법률의 특별한 규정 등 개인정보 보호법 제17조에 해당하는 경우 외에는 개인 정보를 제3자에게 제공하지 않습니다.\n" +
                "\n" +
                "제4조 (정보주체와 법정대리인의 권리.의무 및 행사방법)\n" +
                "정보주체는 핀더가든에 대해 언제든지 다음 각 호의 개인정보 보호 관련 권리를 행사할 수 있습니다. 1. 개인정보 열람요구\n" +
                "2. 개인정보에 오류 등이 있을 경우 정정 요구\n" +
                "3. 삭제요구\n" +
                "4. 처리정지 요구\n" +
                "\n" +
                "제5조 (처리하는 개인정보 항목)\n" +
                "핀더가든은(는) 다음의 개인정보 항목을 처리하고 있습니다.\n" +
                "- (필수) 핀더가든 회원 가입 시 : 휴대폰번호, 비밀번호, 닉네임\n" +
                "\n" +
                "제6조 (자신의 개인정보 열람, 정정 및 삭제)\n" +
                "정보주체는 핀더가든에 대해 언제든지 다음 각호의 개인정보 보호 관련 권리를 행사할 수 있습니다. \n" +
                "1) 개인정보 열람요구\n" +
                "2) 오류 등이 있을 경우 정정 요구\n" +
                "3) 삭제요구\n" +
                "4) 처리정지 요구\n" +
                "정보주체의 개인정보 보호 관련 권리행사는 [개인정보보호법] 시행규칙 별지 제8호 서식에 따라 서면, 전자우편, 모사전송(FAX) 등을 통하여 하실 수 있으며, 핀더가든은(는) 이에 대해 지체 없이 조치하겠습니다.\n" +
                "정보주체는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 권리를 행사하실 수 있습니다. 이 경 우 [개인정보보호법] 시행규칙 별지 제11호 서식에 따른 위임장을 제출하셔야 합니다.\n" +
                "정보주체가 개인정보의 오류 등에 대한 정정 또는 삭제를 요구한 경우 핀더가든은(는) 정정 또는 삭제를 완료할 때까지 당 해 개인정보를 이용하거나 제공하지 않습니다.\n" +
                "계정 및 아이디는 이용약관에 따라 수정에 제한이 있을 수 있습니다. \n" +
                "\n" +
                "제7조 (개인정보의 보유기간 및 이용기간)\n" +
                "이용자의 동의 하에 수집된 개인정보는 회원자격이 유지되는 동안 보유 및 이용되며, 해지를 요청하는 경우에는 재생이 불가능한 방법에 의해 파기됩니다. 단, 상법 및 전자상거래 등에서의 소비자보호에 관한 법률 등에 근거하 여 거래관련 권리 의무 관계를 확인할 필요가 있거나 기타 관련 법률을 이유로 일정기간 보유하여야 할 필요가 있 을 경우에는 일정기간 보유합니다.\n" +
                "(1) 계약 또는 청약철회 등에 관한 기록: 5년\n" +
                "- 보존근거: 전자상거래 등에서의 소비자보호에 관한 법률\n" +
                "(2) 대금결제 및 재화 등의 공급에 관한 기록: 5년\n" +
                "- 보존근거: 전자상거래 등에서의 소비자보호에 관한 법률\n" +
                "(3) 소비자의 불만 또는 분쟁처리에 관한 기록: 3년\n" +
                "- 보존근거: 전자상거래 등에서의 소비자보호에 관한 법률\n" +
                "(4) 웹사이트 방문기록 : 3개월\n" +
                "- 보존근거: 통신비밀보호법\n" +
                "(5) 관계법령이나 핀더가든의 서비스 이용약관을 위반한 불량 이용자에 관한 기록: 1년\n" +
                "\n" +
                "제8조 (개인정보 파기절차 및 방법) 이용자의 개인정보는 아래의 기준에 의해 관리됩니다.\n" +
                "1. 파기대상\n" +
                "1) 수집 및 이용목적이 달성되거나, 보유 및 이용기간이 경과된 개인정보\n" +
                "2) 핀더가든의 서비스를 1년 동안 이용하지 않는 이용자의 개인정보\n" +
                "3) 법령에서 별도의 기간을 정하고 있는 경우 해당 법령에서 정한 기간이 경과한 개인정보\n" +
                "2. 파기절차\n" +
                "이용자가 회원가입 등을 위해 입력한 정보는 목적이 달성된 후 내부 방침 및 기타 관련 법령에 의한 보관 기간 동 안 저장된 후 지체 없이 파기됩니다. 핀더가든의 서비스를 1년 동안 이용하지 않는 이용자의 개인정보 중 서비스 재이 용을 위해 필요한 최소한의 정보는 다른 이용자의 개인정보와 분리되어 별도로 저장‧관리되며, 서비스를 마지막으 로 이용한 시점에서 5년이 경과되면 파기됩니다. 단, 개인정보를 별도로 저장‧관리하는 경우에는 관계법령에 특별 한 규정이 있는 경우를 제외하고는 해당 개인정보를 이용하거나 제공하지 않습니다.\n" +
                "3. 파기방법\n" +
                "1) 종이에 출력된 개인정보는 분쇄기로 분쇄하거나 소각을 통하여 파기됩니다.\n" +
                "2) 전자적 파일 형태로 저장된 개인정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제합니다.\n" +
                "\n" +
                "제9조 (개인정보 자동 수집 장치의 설치∙운영 및 거부에 관한 사항)\n" +
                "1) 핀더가든은(는) 이용자에게 개별적인 맞춤서비스를 제공하기 위해 이용정보를 저장하고 수시로 불러오는 ‘쿠키 (cookie)’를 사용합니다.\n" +
                "2) 쿠키는 웹사이트를 운영하는데 이용되는 서버(http)가 이용자의 컴퓨터 브라우저에게 보내는 소량의 정보이 며 이용자들의 PC 컴퓨터 및 단말기 내의 하드디스크에 저장되기도 합니다.\n" +
                "가. 쿠키의 사용목적: 이용자가 방문한 각 서비스와 웹 사이트들에 대한 방문 및 이용형태, 인기 검색어, 보안접 속 여부, 등을 파악하여 이용자에게 최적화된 정보 제공을 위해 사용됩니다.\n" +
                "나. 쿠키의 설치∙운영 및 거부 : 웹브라우저 상단의 도구>인터넷 옵션>개인정보 메뉴의 옵션 설정을 통해 쿠키 저장을 거부 할 수 있습니다.\n" +
                "다. 쿠키 저장을 거부할 경우 맞춤형 서비스 이용에 어려움이 발생할 수 있습니다. \n" +
                "\n" +
                "제10조 (이용자의 권리와 의무)\n" +
                "이용자의 개인정보를 최신의 상태로 정확하게 입력하여 불의의 사고를 예방해 주시기 바랍니다. 이용자가 입력한 부정확한 정보로 인해 발생하는 사고의 책임은 이용자에게 있으며 타인 정보의 도용 등 허위정보를 입력할 경우 서비스의 이용이 제한될 수 있습니다.\n" +
                "이용자는 개인정보를 보호 받을 권리를 보유하나 이와 동시에 이용자의 정보를 스스로 보호하고 또한 타인의 정보 를 침해하지 않을 의무도 가지고 있습니다. 비밀번호를 포함한 이용자의 개인정보가 유출되지 않도록 조심하시고 게시물을 포함한 타인의 개인정보를 훼손하지 않도록 유의해주십시오.. 만약 이 같은 책임을 다하지 못하고 타인 의 정보 및 존엄성을 훼손할 시에는 [개인정보보호법] 및 [정보통신망이용촉진및정보보호등에관한법률]등에 의해 처벌 받을 수 있습니다.\n" +
                "\n" +
                "제 11조 (의견수렴 및 불만처리)\n" +
                "핀더가든은(는) 이용자의 의견을 소중하게 생각합니다.\n" +
                "개인정보에 관한 문의사항은 전자우편(inseongsong4@gmail.com)으로 접수하여 주십시오. 접수 후, 최단 시간 내에 성실하게 답변해 드리겠습니다. 다만, 근무시간 이후 또는 주말 및 공휴일에는 익일 처리하는 것을 원칙 으로 합니다.\n" +
                "기타 개인정보에 관한 상담이 필요한 경우에는\n" +
                "1) 개인정보분쟁조정위원회 (http://www.kopico.go.kr, 02-2100-2499)\n" +
                "2) 개인정보침해신고센터 (http://privacy.kisa.or.kr, 국번없이 118)\n" +
                "3) 대검찰청 사이버수사과 (http://www.spo.go.kr, 02-3480-3571)\n" +
                "4) 경찰청 사이버안전국 (www.ctrc.go.kr 국번없이 182)) 등으로 문의하실 수 있습니다.\n"


            return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }


}