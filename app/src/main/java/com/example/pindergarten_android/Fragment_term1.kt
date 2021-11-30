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

class Fragment_term1 : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)


    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_term1,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{

            mainAct.onBackPressed()

        }

        var text = view.findViewById<TextView>(R.id.text)
        text.text = "이용약관\n" +
                "최종 개정 : 2021년 11월 25일\n" +
                "\n" +
                "핀더가든을(를) 이용하기 이전에 본 이용약관(이하 “약관”)을 주의깊게 읽어보시기 바랍니다.\n" +
                "\n" +
                "제 1조(목적)\n" +
                "본 약관은 핀더가든(이하 “회사”)가 모바일 기기를 통해 제공하는 산 정복 게임 서비스 및 이에 부수하는 기타 서비스(이하 “서비스”)의 이용에 대한 회사와 귀하의 권리, 의무 및 책임사항, 기타필요한 사항을 규정함을 목적으로 합니다. 또한 서비스를 이용함으로써 귀하는 본 약관과 회사의개인정보 처리방침이 귀하에게 법적 효력이 있음을 인정하고 동의하는 것 입니다.\n" +
                "\n" +
                "제 2조 (용어 정의)\n" +
                "본 약관에서 정하는 것 외의 용어의 정의는 관계법령 및 회사가 정하는 바에 의합니다.\n" +
                "1.\t“이용자”란 본 약관에 따라 이용계약을 체결하고, 회사가 제공하는 모든 콘텐츠 및 제반 서비스를 이용하는 모든 회원을 말합니다.\n" +
                "2.\t“콘텐츠”란 디바이스 등을 통하여 이용할 수 있도록 회사가 제공하는 모든 서비스와 관련되어 제작 및 내용물 일체를 말합니다.\n" +
                "3.\t“서비스”란 유무선 네트워크를 통하여 이용자가 콘텐츠를 이용할 수 있도록 회사가 제공하는 서비스 일체를 말합니다.\n" +
                "4.\t“서비스 신청”이라 함은 회사가 제공하는 서비스를 이용할 수 있도록 신청하는 것을 말합니다\n" +
                "이 약관에서 사용하는 용어의 정의는 본 조 제1항에서 정하는 것을 제외하고는 관계법령 및 서비스별 정책에서 정하는 바에 의하며, 이에 정하지 아니한 것은 일반적인 상관례에 따릅니다.\n" +
                "\n" +
                "제 3조 (약관의 게시와 변경)\n" +
                "1.\t귀하가 당사 회원가입을 하는 경우, 이하의 규칙이 적용되며 회원가입을 한 고객들 모두에게 그 효력이 발생 합니다.\n" +
                "2.\t회사는 약관의 규제 등에 관한 법률, 전자거래기본법, 전자서명법, 정보통신망 이용촉진 및 정보보호 등에 관 한 법률 등 관계법령을 위배하지 않는 범위에서 본 약관을 수시로 수정하거나 변경, 중지 또는 중단 할 수 있 습니다. 앱 초기화면에 공지되면 그 효력이 발생합니다.\n" +
                "3.\t본 약관은 회사의 제반 환경의 변화 또는 정책에 따라 변경이 가능하며, 본 약관을 변경하고자할 경우, 변경 된 약관을 적용하고자 하는 날 (이하 ‘효력 발생일’)로부터 7일(회원에게 불리한 변경의 경우 30일) 이전에 본 약관이 변경된다는 사실과 변경된 내용 등을 앱 초기화면에 공지하고연결화면을 통하여 볼 수 있도록 합니다.\n" +
                "4.\t이용자는 제 3항에 따라 약관 내용에 동의하지 아니하면 언제든지 서비스 이용 계약을 해지할수 있습니다. 단, 개정약관의 적용일 이후 30일 이내 계속적으로 서비스를 이용하는 경우 해당 이용자는 약관의 변경에 동 의한 것으로 간주합니다.\n" +
                "\n" +
                "제 4조 (핀더가든 서비스)\n" +
                "회사는 관련법과 미풍양속에 반하는 행위를 하지 않으며, 계속적이고 안정적으로 서비스를 제공하기 위해 최선을 다해 노력합니다. 본 약관이 금지하는 행위를 하지 않으며 개인정보 보호를 위해 개인정보 보호방침을 및 관계 법 령이 정한 의무사항을 준수합니다.\n" +
                "1.\t회사는 운영이나 기술상 필요에 따라 서비스를 언제든지 수정할 수 있습니다.\n" +
                "2.\t회사는 이용자의 행동, 이행, 과실, 부주의, 작위 또는 부작위에 대하여 책임지지 않습니다.\n" +
                "3.\t이용자가 작성한 서비스 신청에 대한 모든 권리 및 책임은 이를 신청한 이용자에게 있으며, 회사는 이용 자가 신청하거나 등록하는 서비스의 내용물이 다음 각 항에 해당한다고 판단되는 경우에 사전 통지 없이 삭제할 수 있고, 이에 대하여 회사는 어떠한 책임도 지지 않습니다.\n" +
                "불법적인 목적의 경우\n" +
                "다른 이용자 또는 제3자를 비방하거나 명예를 손상시키는 내용인 경우\n" +
                "공공질서 및 미풍양속에 위반되는 내용일 경우\n" +
                "범죄적 행위에 결부된다고 인정되는 경우\n" +
                "회사의 저작권, 제3자의 저작권 등 기타 권리를 침해하는 내용인 경우\n" +
                "음란물을 게재하거나 음란사이트를 링크하는 경우\n" +
                "회사로부터 사전 승인 받지 아니한 상업광고, 판촉 내용을 게시하는 경우\n" +
                "해당 상품과 관련없는 내용인 경우\n" +
                "정당한 사유 없이 당사의 영업을 방해하는 내용을 기재하는 경우\n" +
                "서비스를 차단하거나 손상시켜 서비스가 정상적인 작동범위를 벗어나게 만들려는 경우\n" +
                "다른 유저의 계정에 접근하거나 시도하려는 경우\n" +
                "기타 관계법령에 위반된다고 판단되는 경우\n" +
                "\n" +
                "제 5조 (이용자의 의무)\n" +
                "1.\t이용자는 서비스와 관련하여 개인정보를 안전하게 비밀로 유지·관리하고 제 3자에게 공개하여서는 안됩니다.\n" +
                "2.\t이용자의 본 약관 준수를 전제로, 회사는 이용자에게 앱을 모바일 디바이스에 다운로드, 설치 및 실행할 수 있는 자격을 부여합니다.\n" +
                "3.\t이용자는 회사 업무에 방해되는 행위를 하여서는 안됩니다.\n" +
                "4.\t이용자는 서비스 이용과 관련하여 다음 사항의 행위를 하여서는 안됩니다.\n" +
                "서비스 신청 또는 변경시 허위 내용의 등록\n" +
                "외설 또는 폭력적인 메시지, 화상, 음성 기타 공공질서 미풍양속에 반하는 정보를 회사에 공개 또는 게시하는 행위\n" +
                "판매, 재판매, 할당, 양도, 배포 또는 상업적으로 이용하거나 제 3자가 서비스를 이용할 수 있도록 하는 행위\n" +
                "앱을 변경하거나 이에 의거한 2차적 저작물 내지 결과물을 생성하는 행위\n" +
                "서비스에 무단 접근하는 행위(수정되거나 제3자 소프트웨어의 사용 포함)\n" +
                "앱을 회사가 제공하는 기술 플랫폼외 다른 기술 플랫폼과 연결 또는 연동하거나 디바이스에 환경 조작을 통한 앱의 오동작 혹은 기타 서비스의 본래 의도에 벗어나는 동작을 유도하는 행위\n" +
                "앱을 개작하거나 리버스 엔지니어링, 디컴파일, 디스어셈블 하거나 또는 회사의 승인 없이 임의로 앱을 복제 혹은 복사하여 배포하는 행위\n" +
                "앱에 부담을 발생시키거나 운영 또는 성능을 저해하는 자동 프로그램 실행에 착수하는 행위\n" +
                "기타 관련법령에 위반된다고 판단되는 행위\n" +
                "\n" +
                "제 6조 저작권 등의 귀속\n" +
                "1.\t서비스 내 회사가 제작한 콘텐츠에 대한 저작권 기타 지적재산권은 회사의 소유입니다.\n" +
                "2.\t이용자는 회사가 제공하는 서비스를 이용함으로써 얻은 정보 중 회사 또는 해당 정보의 제공업체에 지적재산권 이 귀속된 정보를 회사 또는 제공업체의 사전 승낙 없이 복제, 전송, 출판, 배포, 방송 기타 방법에 의하여 영리목 적으로 이용하거나 제3자에게 이용하게 하여서는 안 됩니다.\n" +
                "3.\t이용자는 서비스 내에서 보여지거나 서비스와 관련하여 이용자 또는 다른 이용자가 서비스를 통해 업로드 또는 전송하는 대화 텍스트를 포함한 커뮤니케이션, 이미지, 사운드 및모든 자료 및정보(이하 ‘이용자콘텐츠’ )에 대하 여 회사가 다음과 같은 방법과 조건으로 이용하는 것을 허락합니다.\n" +
                "①\t해당 이용자콘텐츠를 이용하거나 편집 형식의 변경 및 기타의 방법으로 변형하는 것(공표, 복제, 공연, 전송, 배 포, 방송, 2차적저작물 작성 등 어떠한 형태로든 이용 가능하며 이용기간과 지역에는 제한이 없음).>\n" +
                "②\t이용자콘텐츠를 제작한 이용자의 사전 동의 없이 거래를 목적으로 이용자콘텐츠를 판매, 대여, 양도하지 않음. \n" +
                "4.\t서비스 내에서 보여지지 않고 서비스와 일체화 되지 않은 이용자의 콘텐츠(예컨대, 프로필 등에서의 게시물)에 대하여 회사는 이용자의 명시적 동의가 없이는 상업적으로 이용하지 않으며, 이용자는 언제든지 이러한 이용자의 콘텐츠를 삭제할 수 있습니다.\n" +
                "5.\t회사는 이용자가 게시하거나 등록하는 서비스 내의 게시물 또는 게시 내용에 대해 이 약관에서 정하는 금지행 위에 해당된다고 판단하는 경우 사전 통지 없이 이를 삭제하거나 이동 또는 등록을 거부할 수 있습니다.\n" +
                "6.\t회사가 운영하는 게시판 등에 게시된 정보로 인하여 법률상 이익이 침해된 이용자는 회사에게 당해 정보의 삭 제 또는 반박 내용의 게재를 요청할 수 있습니다. 이 경우 회사는신속하게 필요한 조치를 취하고 이를 신청인에게 통지합니다.\n" +
                "7.\t상기 3.은 회사가 서비스를 운영하는 동안 유효하며 이용자의 해지 또는 회원 탈퇴 후에도 지속적으로 적용됩 니다.\n" +
                "\n" +
                "제 7조 정보의 제공 및 광고 게재\n" +
                "1.\t회사는 본 서비스 등을 유지하기 위하여 광고를 게재할 수 있으며, 이용자는 서비스 이용 시 노출되는 광고게재 에 대하여 동의합니다.\n" +
                "2.\t회사가 제공하는, 제3자가 주체인, 제1항의 광고에 이용자가 참여하거나 교신 또는 거래를 함으로써 발생하는 손실 또는 손해에 대해서 회사는 어떠한 책임도 부담하지 않습니다.\n" +
                "3.\t회사는 서비스 개선 및 이용자 대상 서비스 소개 등을 위한 목적으로 이용자 개인에 대한 추가정보를 요구할 수 있으며, 동 요청에 대해 이용자는 승낙하여 추가정보를 제공하거나 거부할 수 있습니다.\n" +
                "4.\t회사는 이용자의 사전 동의 하에 이용자로부터 수집한 개인정보를 활용하여 제1항의 광고 및 제3항의 정보 등 을 제공하는 경우 SMS, 스마트폰 알림, e-mail을 활용하여 발송할 수 있으며, 이용자는 원하지 않을 경우 언제든지 수신을 거부할 수 있습니다.\n" +
                "\n" +
                "제 8조 (이용자의 자격 상실)\n" +
                "1.\t회사는 이용자가 본 약관의 의무를 위반하거나 서비스의 정상적인 운영을 고의 또는 과실로 방해한 경우, 즉 시 서비스 이용을 정지할 수 있습니다.\n" +
                "2.\t회사는 앞 조항에도 불구하고 다음 사항의 행위에 대하여 즉시 영구적 이용정지를 할 수있으며, 영구적 이용 정지시 서비스를 통해 획득한 혜택 등도 모두 소멸됩니다. 회사는 이에 대해 어떠한 보상도 하지 않습니다.\n" +
                "주민등록법을 위반한 명의도용 및 결제도용, 전화번호 도용, 이메일 도용\n" +
                "저작권법 및 컴퓨터 프로그램 보호법을 위반한 불법 프로그램 제공 및 운영 방해\n" +
                "정보통신망법을 위반한 통신 및 해킹, 배포\n" +
                "이용자권한 초과행위\n" +
                "관련법 등을 위반한 경우\n" +
                "3.\t이용자는 본 조에 따른 이용제한등에 대해 회사에 이의신청을 할 수 있습니다. 이때 이의신청은 회사가 정한 절차를 따릅니다. 이의가 정당하다고 회사가 인정하는 경우 회사는 서비스의 이용을 재개할 수 있습니다.\n" +
                "\n" +
                "제 9조 (책임의 제한)\n" +
                "회사는 천재지변, 기상, 화재, 홍수, 테러행위 또는 이에 준하는 불가항력 등 회사의 통제를 벗어난 원인에 의하여 서비스를 제공할 수 없는 경우에 손해, 상해, 불이행, 이행지연또는 서비스 제공에 관한 책임이 면제됩니다.\n" +
                "회사는 이용자의 귀책사유로 인한 서비스 중지, 장애에 대하여 책임을 지지 않습니다.\n" +
                "회사는 이용자의 모바일 디바이스 네트워크 환경으로 인하여 발생하는 문제에 대하여 책임이 면제됩니다.\n" +
                "회사는 서비스와 관련하여 이용자 정보의 허위, 오류, 생략, 누락, 사실의 신뢰도, 정확성 등의 내용에 관해서 책임을 지지 않습니다.\n" +
                "이용자 간의 서비스 이용 과정에서 발생하는 명예훼손 및 불법행위로 인한 각종 민형사상 책임을 지지 않습니다.\n" +
                "회사는 서비스를 매개로 이용자 간 또는 이용자와 제3자 간에 거래 등에 관련하여 책임을 지지 않습니다.\n" +
                "서비스에 대한 연결 및 서비스의 이용과정에서 발생하는 문제에 관련하여 책임을 지지 않습니다.\n" +
                "서비스에 에러, 바이러스, 악성 프로그램, 스파이웨어가 없음을 보장하지 않으며 이에 관하여 책임을 지지 않습니다.\n" +
                "제3자의 모든 불법적인 서버 연결 또는 불법적인 이용으로부터 발생하는 내용에 관해서 책임을 지지 않습니다.\n" +
                "회사는 서비스 이용과 관련하여 관련법에 특별한 규정이 없는 한 책임을 지지 않습니다.\n" +
                "\n" +
                "제 10조 (손해배상)\n" +
                "회사는 제공되는 서비스와 관련하여 이용자에게 어떠한 손해가 발생하더라도 회사가 고의로 행한범죄 행위를 제외하고 이에 대하여 책임을 부담하지 않습니다.\n" +
                "이용자는 본 약관의 규정을 위반하여 회사에 손해가 발생한 경우 회사는 이용자에 대하여 손해배상을 청구할 수 있습니다.\n" +
                "\n" +
                "제 11조 (서비스 변경 및 중단)\n" +
                "회사는 서비스의 일부 또는 전부를 회사의 운영상 또는 정책에 의하여 수정, 변경, 중단할 수 있습니다. 이와 관련 하여 본 약관에 특별한 규정이 없는 한 이용자에게 별도의 보상을 하지 않습니다.\n" +
                "\n" +
                "제 12조 (양도금지)\n" +
                "이용자가 서비스의 이용권한을 타인에게 양도, 증여할 수 없으며, 이를 담보로 제공 할 수 없습니다.\n" +
                "\n" +
                "제 13조 (개인정보보호 및 이용)\n" +
                "회사는 서비스를 제공하기 위해서는 이용자에 대한 개인정보가 필요합니다.\n" +
                "회사가 이용자에게 수집하는 개인정보, 정보의 이용, 보관 그리고 양도 등의 자세한 사항에 관한 내용은 개인정보 보호방침을 참조하여 주시기 바랍니다. 개인정보 보호방침은 본 약관에 포함됩니다.\n" +
                "회사는 이용자에게 양질의 서비스를 제공하기 위해 정보를 수집하며, 운영의 필요에 따라 추가적인 정보 를 수집할 수 있습니다.\n" +
                "이용자는 서비스에 등록한 자신의 개인정보에 대해 언제든지 열람하고 수정할 수 있습니다.\n" +
                "회사가 공식적으로 제공하는 서비스 외에 제 3자가 제공하는 앱, 플랫폼, 서비스 등의 개인정보 취급과 관련된 내용에 대한 책임을 부담하지 않습니다.\n" +
                "회사는 이용자의 개인정보 보호를 위해 개인정보 보호법 등 관계 법령에서 정하는 바를 준수합니다.\n" +
                "회사는 정보보호 업무를 위해 정부기관(수사기관 포함)으로부터 개인정보 정보제공을 요청 받는 경우 제3자에게 제공할 수 있습니다.\n" +
                "\n" +
                "제 14조 (면책 조항)\n" +
                "회사는 천재지변, 기상, 화재, 홍수, 테러행위 또는 이에 준하는 불가항력 등 회사의 통제를 벗어난 원인 에 의하여 서비스를 제공할 수 없는 경우에 손해, 상해, 불이행, 이행지연 또는 서비스 제공에 관한 책임 이 면제됩니다.\n" +
                "회사는 이용자의 귀책사유로 인한 서비스 중지, 이용장애, 계약해지에 대하여 책임이 면제됩니다.\n" +
                "회사는 휴대전화 네트워크, 핫스팟, 무선인터넷 및 기타 서비스를 포함하는 제 3자 서비스의 가용성 또는 품질에 대한 책임을 지지 않습니다. 제 3자 서비스로 인하여 또는 어떤 방식으로든 그와 관련하여 발생하는 모든 청구, 요구, 제소원인, 손해, 손실, 비용 또는 책임이 면제됩니다.\n" +
                "회사에서 제공하는 서비스에 에러, 바이러스, 악성 프로그램, 스파이웨어가 없음을 보장하지 않으며 이에 관하여 책임이 면제됩니다.\n" +
                "위치 정보 제공중 어떠한 경우에도 이용자의 손실에 대하여 회사는 책임을 지거나 배상을 하지 않습니다.\n" +
                "회사는 서비스와 관련하여 이용자 정보의 허위, 오류, 생략, 누락, 사실의 신뢰도, 정확성등의 내용에 관해서 책임을 지지 않습니다.\n" +
                "회사는 이용자가 서비스를 이용하여 기대하는 수익의 상실, 손해에 관하여 책임이 면제됩니다.\n" +
                "회사는 서비스를 매개로 이용자 간 또는 이용자와 제3자 간에 거래 등에 관련하여 책임이 면제됩니다.\n" +
                "회사는 위치정보에 대한 신뢰도, 정확성, 부정확성 등을 보장하지 않습니다. 이용자는 위치정보에 결점이 있을 수 있다고 인정하고 승인합니다. 회사는 위치정보의 신뢰도, 정확성, 부정확성 등에 책임이 면제됩니다.\n" +
                "서비스 이용 또는 이용할 수 없음으로 인하여 발생하는 내용에 대하여 회사는 책임이 면제됩니다.\n" +
                "\n" +
                "제 15조 (재판권 및 준거법)\n" +
                "회사와 이용자간에 발생한 분쟁에 관한 소송은 제소 당시의 이용자의 주소에 의하고, 주소가 없는 경우에는 거소를 관할하는 지방법원의 전속관할로 합니다. 다만, 제소 당시 이용자의 주소 또는 거소가 분명 하지 않거나 외국 거주자의 경우에는 민사소송법상의 관할 법원에 제소합니다.\n" +
                "회사와 이용자 간에 제기된 분쟁은 한국법을 적용합니다.\n"



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