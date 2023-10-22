import React, { useState } from "react";
import Header from "../component/header/Header";
import styles from "../css/safetyGuide/SafetyGuide.module.css";
import Footer from "../component/footer/Footer";
import {
  FaPlantWilt,
  FaBridgeWater,
  FaHouseFloodWater,
  FaCampground,
} from "react-icons/fa6";
import { FaSwimmer } from "react-icons/fa";
import { GiWaterSplash } from "react-icons/gi";

const SafetyGuidePage = () => {
  const [category, setCategory] = useState("물놀이 안전수칙");
  const [detailCategory, setDetailCategory] = useState("물놀이 10대 안전수칙");
  const [tipCategory, setTipCategory] = useState("수초에 감겼을 때");

  const categoryArr = [
    "물놀이 10대 안전수칙",
    "안전한 물놀이",
    "물놀이 전 준비 사항",
    "어린이 동반 시 유의사항",
  ];

  const tipArr1 = [
    "수초에 감겼을 때",
    "수영 중 경련이 일어났을 때",
    "하천이나 계곡물을 건널 때",
  ];
  const tipArr2 = [
    "물에 빠졌을 때",
    "침수・고립되었을 때",
    "계곡에서 야영지를 선택할 때",
  ];

  return (
    <div>
      <Header />
      <div className={styles.safetyGuide}>
        <div className={styles.body}>
          <div className={styles.category}>
            <span
              onClick={() => setCategory("물놀이 안전수칙")}
              style={
                category === "물놀이 안전수칙"
                  ? {
                      backgroundColor: "#66a5fc",
                      color: "white",
                    }
                  : {}
              }
            >
              물놀이 안전수칙
            </span>
            <span
              onClick={() => setCategory("물놀이 사고 행동요령")}
              style={
                category === "물놀이 사고 행동요령"
                  ? {
                      backgroundColor: "#66a5fc",
                      color: "white",
                    }
                  : {}
              }
            >
              물놀이 사고 행동요령
            </span>
          </div>
          {category === "물놀이 안전수칙" ? (
            <div className={styles.guideContainer}>
              <div className={styles.detailCategory}>
                {categoryArr.map((item) => {
                  return (
                    <div>
                      <span
                        onClick={() => setDetailCategory(item)}
                        style={
                          detailCategory === item ? { color: "black" } : {}
                        }
                      >
                        {item}
                      </span>
                    </div>
                  );
                })}
              </div>
              <div className={styles.guide}>
                {detailCategory === "물놀이 10대 안전수칙" ? (
                  <>
                    <p>
                      <span>01</span> 수영을 하기 전에는 손, 발 등의 경련을
                      방지하기 위해 반드시 준비운동을 하고 구명조끼를 착용한다.
                    </p>
                    <p>
                      <span>02</span> 물에 처음 들어가기 전 심장에서 먼
                      부분부터(다리, 팔, 얼굴, 가슴 등의 순서) 물을 적신 후
                      들어간다.
                    </p>
                    <p>
                      <span>03</span> 수영도중 몸에 소름이 돋고 피부가 당겨질
                      때에는 몸을 따뜻하게 감싸고 휴식을 취한다.
                    </p>
                    <p>
                      <span>04</span> 물의 깊이는 일정하지 않기 때문에 갑자기
                      깊어지는 곳은 특히 위험하다.
                    </p>
                    <p>
                      <span>05</span> 구조 경험이 없는 사람은 안전구조 이전에
                      무모한 구조를 삼가해야 한다.
                    </p>
                    <p>
                      <span>06</span> 물에 빠진 사람을 발견하면 주위에 소리쳐
                      알리고(즉시 119에 신고) 구조에 자신이 없으면 함부로 물속에
                      뛰어 들지 않는다.
                    </p>
                    <p>
                      <span>07</span> 수영에 자신이 있더라도 가급적 주위의
                      물건들(튜브, 스티로폼, 장대 등)을 이용한 안전구조를 한다.
                    </p>
                    <p>
                      <span>08</span> 건강 상태가 좋지 않을 때나, 몹시 배가
                      고프거나 식사 후에는 수영을 하지 않는다.
                    </p>
                    <p>
                      <span>09</span> 자신의 수영능력을 과신하여 무리한 행동을
                      하지 않는다.
                    </p>
                    <p>
                      <span>10</span> 장시간 계속 수영하지 않으며, 호수나
                      강에서는 혼자 수영하지 않는다.
                    </p>
                    <p>
                      <span>11</span> 어린이 물놀이 안전을 위해 주의해야 할 것들
                    </p>
                    <div>
                      <span>
                        • 보호자는 어린이를 항상 확인 가능한 시야 내에서 놀도록
                        한다.
                      </span>
                      <span>
                        • 구명조끼를 착용하고, 물놀이 중에는 껌이나 사탕을 먹지
                        않는다.
                      </span>
                      <span>
                        • 친구를 밀거나 물속에 발을 잡는 장난을 치지 않는다.
                      </span>
                      <span>
                        • 신발 등의 물건이 떠내려가도 절대 혼자 따라가서 건지려
                        하지 말고 어른에게 도움을 요청한다.
                      </span>
                    </div>
                  </>
                ) : detailCategory === "안전한 물놀이" ? (
                  <>
                    <p>
                      <span>01</span> 물이 갑자기 깊어지는 곳은 특히 위험하다.
                    </p>
                    <p>
                      <span>02</span> 건강 상태가 좋지 않을 때, 배가 고플 때,
                      식사 후에 수영을 하지 않는다.
                    </p>
                    <p>
                      <span>03</span> 수영능력 과신은 금물, 무모한 행동은 하지
                      않는다.
                    </p>
                    <p>
                      <span>04</span> 장시간 수영을 하지 않는다. 호수나 강에서
                      혼자 수영하지 않는다.
                    </p>
                    <p>
                      <span>05</span> 물에 빠진 사람을 발견하면 주위에 소리쳐
                      알리고 즉시 119에 신고 한다.
                    </p>
                    <p>
                      <span>06</span> 구조 경험 없는 사람은 무모한 구조를 하지
                      않는다. 함부로 물에 뛰어들지 않는다.
                    </p>
                    <p>
                      <span>07</span> 가급적 튜브, 장대 등 주위 물건을 이용하여
                      안전하게 구조를 한다.
                    </p>
                  </>
                ) : detailCategory === "물놀이 전 준비 사항" ? (
                  <>
                    <p>
                      <span>01</span> 일반적으로 수영하기에 알맞은 수온은 25~26℃
                      정도 입니다.
                    </p>
                    <p>
                      <span>02</span> 물에 들어갈 때는 다음 사항을 꼭 지켜야
                      합니다.
                    </p>
                    <p>
                      <span>03</span> 준비운동을 한 다음 다리부터 서서히 들어가
                      몸을 순환시키고 수온에 적응시켜 수영하기 시작한다.
                    </p>
                    <p>
                      <span>04</span> 초보자는 수심이 얕다고 안심해서는 안 된다.
                    </p>
                    <div>
                      <span>
                        ※ 물놀이 미끄럼틀에서 내린 후 무릎 정도의 얕은 물인데도
                        허우적대며 물을 먹는 사람들을 자주 볼 수 있으므로 절대
                        안전에 유의한다.
                      </span>
                    </div>
                    <p>
                      <span>05</span> 배 혹은 떠 있는 큰 물체 밑을 헤엄쳐
                      나간다는 것은 위험하므로 하지 않는다.
                    </p>
                    <div>
                      <span>
                        ※ 숨을 들이쉰 상태에서 부력으로 배 바닥에 눌려
                        빠져나오기 어려울 때는 숨을 내뱉으면 몸이 아래로
                        가라앉기 때문에 배 바닥에서 떨어져 나오기 쉽다.
                      </span>
                    </div>
                    <p>
                      <span>06</span> 통나무 같은 의지 물이나 부유구, 튜브 등을
                      믿고 자신의 능력 이상 깊은 곳으로 나가지 않는다.
                    </p>
                    <div>
                      <span>
                        ※ 의지할 것을 놓치거나 부유구에 이상이 생길 수 있다.
                      </span>
                    </div>
                    <p>
                      <span>07</span> 수영 중에 “살려 달라”라고 장난하거나
                      허우적거리는 흉내를 내지 않는다.
                    </p>
                    <div>
                      <span>
                        ※ 주위의 사람들이 장난으로 오인하여 사고로 이어질 수
                        있다.
                      </span>
                    </div>
                    <p>
                      <span>08</span> 자신의 체력과 능력에 맞게 물놀이를 한다.
                    </p>
                    <div>
                      <span>
                        ※ 물에서 평영 50m는 육상에서 250m를 전속력으로 달리는
                        것과 같은 피로를 느낀다.
                      </span>
                    </div>
                    <p>
                      <span>09</span> 껌을 씹거나 음식물을 입에 문 채로 수영하지
                      않는다.
                    </p>
                    <div>
                      <span>※ 기도를 막아 질식의 위험이 있다.</span>
                    </div>
                  </>
                ) : (
                  <>
                    <p>
                      <span>01</span> 어른들이 얕은 물이라고 방심하게 되는
                      그곳이 가장 위험 할 수 있다.
                    </p>
                    <p>
                      <span>02</span> 동물 모양을 하고 보행기처럼 다리를 끼우는
                      방식의 튜브사용은 뒤집힐 때 아이 스스로 빠져나오지 못하고
                      머리가 물속에 잠길 수 있다.
                    </p>
                    <p>
                      <span>03</span> 보호자와 함께 하는 활동 안에서만 안전이
                      보장될 수 있으며, 어린이는 순간적으로 짧은 시간 안에
                      익사할 수 있다는 점을 명심해야 한다.
                    </p>
                    <p>
                      <span>04</span> 어린이와 관련된 수난사고는 어른들의
                      부주의와 감독 소홀에 의해 발생할 수 있다.
                    </p>
                    <p>
                      <span>05</span> 인지능력과 신체 적응력이 떨어지는 유아와
                      어린이들은 보호자의 손을 뻗어 즉각 구조가 가능한 위치에서
                      감독해야 한다.
                    </p>
                    <p>
                      <span>06</span> 활동반경이 넓어지는 만 6~9세 이하
                      어린이들은 보호자의 통제권을 벗어나려는 경향을 보이므로
                      사전 안전교육과 주의를 주어 통제한다.
                    </p>
                  </>
                )}
              </div>
            </div>
          ) : (
            <div className={styles.behaviorTips}>
              <div className={styles.iconContainer}>
                <div className={styles.iconsList}>
                  {tipArr1.map((item, index) => {
                    return (
                      <div
                        onClick={() => {
                          setTipCategory(item);
                        }}
                      >
                        <span
                          style={
                            tipCategory === item
                              ? { color: "#66a5fc", borderColor: "#66a5fc" }
                              : {}
                          }
                        >
                          {index === 0 ? (
                            <FaPlantWilt size="70px" />
                          ) : index === 1 ? (
                            <FaSwimmer size="70px" />
                          ) : (
                            <FaBridgeWater size="70px" />
                          )}
                        </span>
                        <span
                          style={
                            tipCategory === item ? { color: "#66a5fc" } : {}
                          }
                        >
                          {item}
                        </span>
                      </div>
                    );
                  })}
                </div>
                <div className={styles.iconsList}>
                  {tipArr2.map((item, index) => {
                    return (
                      <div
                        onClick={() => {
                          setTipCategory(item);
                        }}
                      >
                        <span
                          style={
                            tipCategory === item
                              ? { color: "#66a5fc", borderColor: "#66a5fc" }
                              : {}
                          }
                        >
                          {index === 0 ? (
                            <GiWaterSplash size="70px" />
                          ) : index === 1 ? (
                            <FaHouseFloodWater size="70px" />
                          ) : (
                            <FaCampground size="70px" />
                          )}
                        </span>
                        <span
                          style={
                            tipCategory === item ? { color: "#66a5fc" } : {}
                          }
                        >
                          {item}
                        </span>
                      </div>
                    );
                  })}
                </div>
              </div>

              <div className={styles.behaviorTipsContent}>
                {tipCategory === "수초에 감겼을 때" ? (
                  <>
                    <p>
                      <span>▪︎</span>
                      <p>
                        수초에 감겼을 때는 부드럽게 서서히 팔과 다리를 움직여
                        풀어야 하고 만약 물 흐름이 있으면 흐름에 맡기고 잠깐만
                        조용히 기다리면 감긴 수초가 헐거워지므로 이때 털어
                        버리듯이 풀고 수상으로 나온다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        놀라서 발버둥칠 경우 오히려 더 휘감겨서 위험에 빠질 수
                        있으므로 침착하게 여유를 가지고 호흡하며, 서서히
                        부드럽게 몸을 수직으로 움직이면서 꾸준히 헤어나오도록
                        한다.
                      </p>
                    </p>
                  </>
                ) : tipCategory === "수영 중 경련이 일어났을 때" ? (
                  <>
                    <p>
                      <span>▪︎</span>
                      <p>
                        경련은 물이 차거나 피로한 근육에 가장 일어나기 쉽고
                        수영하는 사람은 수영 중 그러한 상황에 항시 놓여 있으므로
                        흔히 발생할 수 있다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        경련이 잘 일어나는 부위는 발가락과 손가락이고 넓적다리
                        부위에서도 발생하며, 식사 후 너무 빨리 수영을 하였을
                        때에는 위경련이 일어날 수 있다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        경련이 일어나면 먼저 몸의 힘을 빼서 편한 자세가 되도록
                        하고 (당황하여 벗어나려고 하면 더 심한 경련이 일어난다.)
                        경련 부위를 주무른다. 특히 위경련은 위급한 상황이므로
                        신속히 구급요청을 한다.
                      </p>
                    </p>
                  </>
                ) : tipCategory === "하천이나 계곡물을 건널 때" ? (
                  <>
                    <p>
                      <span>▪︎</span>
                      <p>
                        물결이 완만한 장소를 선정하여, 될 수 있으면 바닥을
                        끌듯이 이동한다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>시선은 건너편 강변 둑을 바라보고 건너야 한다.</p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>이동 방향에 돌이 있으면 될 수 있으면 피해서 간다.</p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        다른 물체를 이용 수심을 재면서 이동한다.(지팡이를 약간
                        상류 쪽에 짚는다)
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        물의 흐름에 따라 이동하되 물살이 셀 때는 물결을 약간
                        거슬러 이동한다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        건너편 하류 쪽으로 밧줄(로프)을 설치하고 한 사람씩
                        건넌다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        밧줄(로프)은 물 위로 설치한다. 밧줄이 없을 때 여러
                        사람이 손을 맞잡거나 어깨를 지탱하고 물 흐르는 방향과
                        나란히 서서 건넌다.
                      </p>
                    </p>
                  </>
                ) : tipCategory === "물에 빠졌을 때" ? (
                  <>
                    <p>
                      <span>▪︎</span>
                      <p>
                        흐르는 물에 빠졌을 때는 물의 흐름에 따라 표류하며
                        비스듬히 헤엄쳐 나온다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        옷과 구두를 신은 채 물에 빠졌을 때는 심호흡을 한 후
                        물속에서 새우등 뜨기 자세를 취한 다음 벗기 쉬운 것부터
                        차례로 벗고 헤엄쳐 나온다.
                      </p>
                    </p>
                  </>
                ) : tipCategory === "침수・고립되었을 때" ? (
                  <>
                    <p>
                      <span>▪︎</span>
                      <p>
                        부유물 등을 이용하며, 특히 배수구나 하수구에 빠지지
                        않도록 유의한다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        도로 중앙지점을 이용하고 될 수 있으면 침수 반대 방향이나
                        측면 방향으로 이동한다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        자기 체온 유지에 관심을 둬야 하며 무리한 탈출 행동을
                        삼간다.
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        가능한 모든 방법을 이용하여 구조 신호를 한다(옷이나
                        화염을 이용)
                      </p>
                    </p>
                    <p>
                      <span>▪︎</span>
                      <p>
                        가능하다면 라디오나 방송을 청취하여 상황에 대처한다.
                      </p>
                    </p>
                  </>
                ) : (
                  <>
                    <p>
                      <span>▪︎</span>
                      <p>
                        수초에 감겼을 때는 부드럽게 서서히 팔과 다리를 움직여
                        풀계곡에서 야영지를 선택할 때는 물이 흘러간 가장 높은
                        흔적보다 위쪽에 있도록 하고, 대피할 수 있는 고지대와
                        대피로가 확보된 곳을 선정하며 또한 낙석 위험과 산사태
                        위험이 없는 곳이어야 한다. ※ 물놀이 사고 및 안전사고
                        발생 때 즉시 119 (해상 122) 또는 1588-3650으로
                        신고하시기 바랍니다.
                      </p>
                    </p>
                  </>
                )}
              </div>
            </div>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default SafetyGuidePage;
