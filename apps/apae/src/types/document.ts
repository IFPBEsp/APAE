export type DocumentWithUrl = {
  id: string;
  name: string;
  category: string;
  type: string;
  owner: string;
  year: number;
  url: string;
};

export type DocumentWithOutUrl = {
  id: string;
  name: string;
  category: string;
  type: string;
  owner: string;
  year: number;
};
