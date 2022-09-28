import { css } from "@emotion/react";
import styled from "@emotion/styled";

import startup from "@/assets/corps/1.png";
import nft1 from "@/assets/nfts/2.png";
import nft2 from "@/assets/nfts/3.png";
import nft3 from "@/assets/nfts/4.png";

import type { Startup } from "@/types/api_responses";
import type { CarouselItem } from "@/types/props";
import Grid from "@/components/Grid";
import CorporationCard from "@/components/Card/CorporationCard";
import Carousel from "@/components/Carousel";
import contracts from "@/contracts/utils";
import SelectTab from "@/components/SelectTab";
import { useSelectTab } from "@/hooks";

export default function InvestmentList() {
  const { account } = contracts.useAccount();
  const menus = ["전체 목록", "Top 10"];
  const { selectedMenu, onSelectHandler } = useSelectTab(menus);

  const carouselItems: CarouselItem[] = [
    {
      startupName: "RENGA",
      image: nft1,
    },
    {
      startupName: "DigiDaigaku",
      image: nft2,
    },
    {
      startupName: "Hume Genesis",
      image: nft3,
    },
  ];
  const startups: Startup[] = [
    {
      startupId: 1,
      startupName: "Samsung NEXT",
      profileImage: startup,
      title: "SNKRZ",
      dueDate: "2022.09.07",
      nftTargetCount: 33,
      nftReserveCount: 1,
    },
    {
      startupId: 2,
      startupName: "Samsung NEXT",
      profileImage: startup,
      title: "SNKRZ",
      dueDate: "2022.09.07",
      nftTargetCount: 33,
      nftReserveCount: 10,
    },
    {
      startupId: 3,
      startupName: "Samsung NEXT",
      profileImage: startup,
      title: "SNKRZ",
      dueDate: "2022.09.07",
      nftTargetCount: 33,
      nftReserveCount: 20,
    },
    {
      startupId: 4,
      startupName: "Samsung NEXT",
      profileImage: startup,
      title: "SNKRZ",
      dueDate: "2022.09.07",
      nftTargetCount: 33,
      nftReserveCount: 0,
    },
  ];

  return (
    <>
      <Carousel items={carouselItems} />
      {/* <PageHeader>
        <Text
          css={css`
            font-size: 20px;
            font-weight: 600;
          `}
          as="h1"
        >
          등록된 펀딩 목록
        </Text>
        <Button
          onClick={() => console.log(account)}
          css={css`
            width: 6rem;
            height: 3rem;
            display: flex;
            justify-content: center;
            align-items: center;
          `}
          type={"blue"}
        >
          <FontAwesomeIcon
            css={css`
              height: 100%;
              margin-right: 0.5rem;
            `}
            icon={faArrowDownWideShort}
          />
          필터
        </Button>
      </PageHeader> */}
      <SelectTab
        menus={menus}
        onSelectHandler={onSelectHandler}
        type={"purple"}
        css={css`
          margin-top: 2rem;
        `}
      />

      <Grid>
        {startups.map((startup) => (
          <CorporationCard
            key={startup.startupId}
            startupName={startup.startupName}
            profileImage={startup.profileImage}
            title={startup.title}
            dueDate={startup.dueDate}
            nftTargetCount={startup.nftTargetCount}
            progress={parseInt(
              (
                (startup.nftReserveCount / startup.nftTargetCount) *
                100
              ).toFixed(0)
            )}
            clickable={true}
          />
        ))}
      </Grid>
    </>
  );
}

const PageHeader = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 2rem 0 0 0;
`;
