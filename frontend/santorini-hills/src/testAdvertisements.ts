import { Advertisement } from "./models/advertisement";
export const testAdvertisements: Advertisement[] = [
  {
    id: 1,
    title: "Beautiful apartment near the beach",
    description: "Spacious 2-bedroom apartment with stunning sea views, just 5 minutes walk from the beach.",
    price: 1200,
    status: "available",
    publicationDate: new Date("2023-10-15"),
    property: {
      type: "apartment",
      location: { latitude: 36.417, longitude: 25.432 },
      address: "Oceanview Ave 123, Santorini",
      area: 85,
      bathrooms: 2,
      bedrooms: 2
    },
    images: [
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5058c24_infocdn__n8fawnjfamtj9cynyzt4gus4p7rxa04egkhenvtvjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d511c4f9_infocdn__hi36enq3pei2ptagpju82swjess6yd3kqxmx0lmejpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d527e7fe_infocdn__axdsghlnvm6hzno88bdtkb0bg8jzxm3vou3el4ljjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d531fb77_infocdn__bioeyek0enhdmoymoavbcuawxmeabqtqqolvlnghjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d545cc75_infocdn__rjaigtvlggeroefv9mnz1ip1so55vckoqwdkq42fjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d550a886_infocdn__qnkvj8pwp8yuoicrjmx3dz21hknyr5qsgbiqwdt0jpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5865f82_infocdn__oybnbdppflyrgiablczdragfp2qqnnen6ufwkbmrjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5d22967_infocdn__wdcss3farpl7znpds6vpwxb77zuypgiwjicccvr0jpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5f6cf77_infocdn__yqlosooq8kximrm3syblrngodrwksbcysgtjg0jvjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d612c4e9_infocdn__muaih5abm5kjpovrqkebedrujwzgnhp5iwosl3jyjpg.jpg",
    ],
    owner: {
      id: "1",
      name: "Maria Papadopoulos",
      email: "maria@example.com",
      password: "securepassword1",
      phone: "+30 693 123 4567",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 2,
    title: "Cozy studio in the heart of Fira",
    description: "Modern studio apartment with all amenities in the center of Fira. Perfect for couples.",
    price: 800,
    status: "available",
    publicationDate: new Date("2023-10-20"),
    property: {
      type: "studio-apartment",
      location: { latitude: 36.416, longitude: 25.433 },
      address: "Fira Central 45, Santorini",
      area: 45,
      bathrooms: 1,
      bedrooms: 0
    },
    images: [
      "https://a0.muscache.com/im/pictures/50648027-8d64-4aaf-aee8-10f8f1da3b0b.jpg?im_w=1200",
      "https://a0.muscache.com/im/pictures/miso/Hosting-781337955812915390/original/5ad81f77-a142-4029-b14f-40cf8ccb7cdc.jpeg?im_w=1440",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d527e7fe_infocdn__axdsghlnvm6hzno88bdtkb0bg8jzxm3vou3el4ljjpg.jpghttps://a0.muscache.com/im/pictures/miso/Hosting-781337955812915390/original/782e6d10-a48d-4567-9ab9-885a79649bff.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-781337955812915390/original/8739637b-bee6-4675-ab3b-3cac8d976309.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-781337955812915390/original/ab5132b5-1ff4-45a3-a197-448b736ddf8f.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-781337955812915390/original/48964b50-4eef-4178-854c-7eb181479790.jpeg?im_w=1440",
    ],
    owner: {
      id: "456",
      name: "Nikos Andreou",
      email: "nikos@example.com",
      password: "securepassword2",
      phone: "+30 697 765 4321",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 3,
    title: "Traditional cave house with caldera view",
    description: "Experience authentic Santorini living in this beautifully restored cave house with breathtaking caldera views.",
    price: 1800,
    status: "pending",
    publicationDate: new Date("2023-10-25"),
    property: {
      type: "house",
      location: { latitude: 36.461, longitude: 25.375 },
      address: "Caldera Edge 78, Oia, Santorini",
      area: 120,
      bathrooms: 2,
      bedrooms: 3
    },
    images: [
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/240fe7b9-5f1e-4749-bc86-f79f5c1cd72a.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/12a0d847-6fd7-4c21-99b6-1c20cf17f09f.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/8cdcd51b-6bc5-4317-8c59-6ff731993527.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/e4688bcd-6990-4c1d-a1ab-ffec93c50017.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/96ceb7d4-57e3-4587-bf8a-da46571c0ee2.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/fedfbf25-b887-4458-8326-3457eb0884e3.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/prohost-api/Hosting-4786882/original/5b087f48-1927-4cab-bf60-dfb4e0e9a430.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/19bf5cc3-71e5-40b0-aa37-791ee05c2dc3.jpeg?im_w=1440",
    ],
    owner: {
      id: "84567",
      name: "Elena Dimitriou",
      email: "elena@example.com",
      password: "securepassword3",
      phone: "+30 698 222 3333",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 4,
    title: "Luxury villa with private pool",
    description: "Indulge in this exclusive villa featuring a private infinity pool overlooking the Aegean Sea.",
    price: 3500,
    status: "available",
    publicationDate: new Date("2023-11-01"),
    property: {
      type: "house",
      location: { latitude: 36.398, longitude: 25.459 },
      address: "Luxury Lane 10, Kamari, Santorini",
      area: 250,
      bathrooms: 4,
      bedrooms: 5
    },
    images: [
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTIxNzU2OTEzMzE1NzU4OTE5NQ==/original/6d34fd9f-2329-43a6-ab55-8ab38c9c00e6.jpeg?im_w=1200",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTIxNzU2OTEzMzE1NzU4OTE5NQ==/original/1283983e-3ee6-44ec-8b12-be6d25005a9d.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTIxNzU2OTEzMzE1NzU4OTE5NQ==/original/c28efb8c-17e5-4e44-94e4-385f8c9522eb.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTIxNzU2OTEzMzE1NzU4OTE5NQ==/original/48fbb101-1605-41ce-ae9c-d3025b4f35c6.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTIxNzU2OTEzMzE1NzU4OTE5NQ==/original/dc7ceb4c-fbbd-4477-a4bb-79a6ff687b81.jpeg?im_w=1440",
    ],
    owner: {
      id: "1",
      name: "Andreas Roussos",
      email: "andreas@example.com",
      password: "securepassword4",
      phone: "+30 691 555 6666",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 5,
    title: "Budget-friendly room in shared house",
    description: "Affordable private room in a shared house, ideal for solo travelers on a budget.",
    price: 400,
    status: "available",
    publicationDate: new Date("2023-11-05"),
    property: {
      type: "room",
      location: { latitude: 36.417, longitude: 25.437 },
      address: "Backpackers St 55, Fira, Santorini",
      area: 18,
      bathrooms: 1,
      bedrooms: 1
    },
    images: [
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/307c352c-4941-40b1-9b82-a85b5f2fad47.jpeg?im_w=1200",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/0ed36ed9-0014-4414-a163-103c8d5f52e8.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/d8e811b3-1542-4fd3-92fc-b045becc39ea.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/e8daf8a4-7f35-4aa3-8975-49a08254eb15.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/c846e446-b59a-40d4-8530-5f77352306b0.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/184d4086-315d-4b4b-9c2a-e1b93ac27a04.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/03a473d4-9eef-4894-872f-9f577e120885.jpeg?im_w=1440",
    ],
    owner: {
      id: "1234",
      name: "Kostas Georgiou",
      email: "kostas@example.com",
      password: "securepassword5",
      phone: "+30 694 111 2222",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 6,
    title: "Rustic cabin with vineyard views",
    description: "Experience the agricultural side of Santorini in this charming cabin surrounded by vineyards. Experience the agricultural side of Santorini in this charming cabin surrounded by vineyards. Experience the agricultural side of Santorini in this charming cabin surrounded by vineyards. Experience the agricultural side of Santorini in this charming cabin surrounded by vineyards. Experience the agricultural side of Santorini in this charming cabin surrounded by vineyards. ",
    price: 750,
    status: "available",
    publicationDate: new Date("2023-11-10"),
    property: {
      type: "cabin",
      location: { latitude: 36.403, longitude: 25.466 },
      address: "Vineyard Path 23, Pyrgos, Santorini",
      area: 60,
      bathrooms: 1,
      bedrooms: 1
    },
    images: [
      "https://a0.muscache.com/im/pictures/77e5c035-80bc-422e-8dca-8cd24f8983da.jpg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-1045347397943323289/original/3ef3a976-9659-45e3-856e-a83082d0b9a4.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTA0NTM0NzM5Nzk0MzMyMzI4OQ%3D%3D/original/6bab55d8-486d-4927-92a4-3d3dfeb0915b.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTA0NTM0NzM5Nzk0MzMyMzI4OQ%3D%3D/original/110c9512-9519-4cc5-a629-06ff9c881de1.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTA0NTM0NzM5Nzk0MzMyMzI4OQ%3D%3D/original/dee211c9-bcb8-4db0-961f-cbac0788df56.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6MTA0NTM0NzM5Nzk0MzMyMzI4OQ%3D%3D/original/2c667421-617d-40b4-9a04-3494e1e524bb.jpeg?im_w=1440",
    ],
    owner: {
      id: "1",
      name: "Dimitris Antonopoulos",
      email: "dimitris@example.com",
      password: "securepassword6",
      phone: "+30 695 777 8888",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 7,
    title: "Seafront apartment with direct beach access",
    description: "Wake up to the sound of waves in this modern apartment with a private path to the beach.",
    price: 1500,
    status: "taken",
    publicationDate: new Date("2023-11-15"),
    property: {
      type: "apartment",
      location: { latitude: 36.378, longitude: 25.471 },
      address: "Beach Boulevard 34, Perissa, Santorini",
      area: 90,
      bathrooms: 2,
      bedrooms: 2
    },
    images: [
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/e4688bcd-6990-4c1d-a1ab-ffec93c50017.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/8cdcd51b-6bc5-4317-8c59-6ff731993527.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/240fe7b9-5f1e-4749-bc86-f79f5c1cd72a.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/12a0d847-6fd7-4c21-99b6-1c20cf17f09f.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/96ceb7d4-57e3-4587-bf8a-da46571c0ee2.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/fedfbf25-b887-4458-8326-3457eb0884e3.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/prohost-api/Hosting-4786882/original/5b087f48-1927-4cab-bf60-dfb4e0e9a430.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/hosting/Hosting-U3RheVN1cHBseUxpc3Rpbmc6NDc4Njg4Mg%3D%3D/original/19bf5cc3-71e5-40b0-aa37-791ee05c2dc3.jpeg?im_w=1440",
    ],
    owner: {
      id: "1",
      name: "Sofia Papas",
      email: "sofia@example.com",
      password: "securepassword7",
      phone: "+30 696 333 4444",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 8,
    title: "Development lot with building permit",
    description: "Prime investment opportunity: lot with approved building permit for a 4-unit complex.",
    price: 250000,
    status: "available",
    publicationDate: new Date("2023-11-20"),
    property: {
      type: "lot",
      location: { latitude: 36.411, longitude: 25.445 },
      address: "Development Zone 100, Messaria, Santorini",
      area: 1000,
      bathrooms: 0,
      bedrooms: 0
    },
    images: [
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/e8daf8a4-7f35-4aa3-8975-49a08254eb15.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/307c352c-4941-40b1-9b82-a85b5f2fad47.jpeg?im_w=1200",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/0ed36ed9-0014-4414-a163-103c8d5f52e8.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/d8e811b3-1542-4fd3-92fc-b045becc39ea.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/c846e446-b59a-40d4-8530-5f77352306b0.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/184d4086-315d-4b4b-9c2a-e1b93ac27a04.jpeg?im_w=1440",
      "https://a0.muscache.com/im/pictures/miso/Hosting-48748370/original/03a473d4-9eef-4894-872f-9f577e120885.jpeg?im_w=1440",
    ],
    owner: {
      id: "1",
      name: "Giorgos Makris",
      email: "giorgos@example.com",
      password: "securepassword8",
      phone: "+30 697 999 0000",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 9,
    title: "Boutique farm with organic garden",
    description: "Live sustainably in this boutique farm featuring an organic garden and small vineyard.",
    price: 2200,
    status: "available",
    publicationDate: new Date("2023-11-25"),
    property: {
      type: "farm",
      location: { latitude: 36.408, longitude: 25.480 },
      address: "Countryside Route 67, Mesa Gonia, Santorini",
      area: 5000,
      bathrooms: 2,
      bedrooms: 3
    },
    images: [
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5058c24_infocdn__n8fawnjfamtj9cynyzt4gus4p7rxa04egkhenvtvjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d511c4f9_infocdn__hi36enq3pei2ptagpju82swjess6yd3kqxmx0lmejpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d527e7fe_infocdn__axdsghlnvm6hzno88bdtkb0bg8jzxm3vou3el4ljjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d531fb77_infocdn__bioeyek0enhdmoymoavbcuawxmeabqtqqolvlnghjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d545cc75_infocdn__rjaigtvlggeroefv9mnz1ip1so55vckoqwdkq42fjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d550a886_infocdn__qnkvj8pwp8yuoicrjmx3dz21hknyr5qsgbiqwdt0jpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5865f82_infocdn__oybnbdppflyrgiablczdragfp2qqnnen6ufwkbmrjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5d22967_infocdn__wdcss3farpl7znpds6vpwxb77zuypgiwjicccvr0jpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5f6cf77_infocdn__yqlosooq8kximrm3syblrngodrwksbcysgtjg0jvjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d612c4e9_infocdn__muaih5abm5kjpovrqkebedrujwzgnhp5iwosl3jyjpg.jpg",
    ],
    owner: {
      id: "1",
      name: "Anna Kyriakidou",
      email: "anna@example.com",
      password: "securepassword9",
      phone: "+30 698 444 5555",
      profile: "owner",
      isVerified: true
    }
  },
  {
    id: 10,
    title: "Modern penthouse with panoramic views",
    description: "Luxurious penthouse apartment with state-of-the-art amenities and 360° island views.",
    price: 2500,
    status: "pending",
    publicationDate: new Date("2023-12-01"),
    property: {
      type: "apartment",
      location: { latitude: 36.422, longitude: 25.431 },
      address: "Heights Tower 15, Fira, Santorini",
      area: 150,
      bathrooms: 3,
      bedrooms: 3
    },
    images: [
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5058c24_infocdn__n8fawnjfamtj9cynyzt4gus4p7rxa04egkhenvtvjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d511c4f9_infocdn__hi36enq3pei2ptagpju82swjess6yd3kqxmx0lmejpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d527e7fe_infocdn__axdsghlnvm6hzno88bdtkb0bg8jzxm3vou3el4ljjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d531fb77_infocdn__bioeyek0enhdmoymoavbcuawxmeabqtqqolvlnghjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d545cc75_infocdn__rjaigtvlggeroefv9mnz1ip1so55vckoqwdkq42fjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d550a886_infocdn__qnkvj8pwp8yuoicrjmx3dz21hknyr5qsgbiqwdt0jpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5865f82_infocdn__oybnbdppflyrgiablczdragfp2qqnnen6ufwkbmrjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5d22967_infocdn__wdcss3farpl7znpds6vpwxb77zuypgiwjicccvr0jpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d5f6cf77_infocdn__yqlosooq8kximrm3syblrngodrwksbcysgtjg0jvjpg.jpg",
      "https://cdn2.infocasas.com.uy/repo/img/th.outside1504x596.678f0d612c4e9_infocdn__muaih5abm5kjpovrqkebedrujwzgnhp5iwosl3jyjpg.jpg",
    ],
    owner: {
      id: "1",
      name: "Petros Economou",
      email: "petros@example.com",
      password: "securepassword10",
      phone: "+30 693 888 9999",
      profile: "owner",
      isVerified: true
    }
  }
];