import { AdvertisementResponse } from "./models/advertisement";
export const testAdvertisements: AdvertisementResponse[] = [
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
    description: "Experience the agricultural side of Santorini in this charming cabin surrounded by vineyards.",
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
      name: "Petros Economou",
      email: "petros@example.com",
      password: "securepassword10",
      phone: "+30 693 888 9999",
      profile: "owner",
      isVerified: true
    }
  }
];