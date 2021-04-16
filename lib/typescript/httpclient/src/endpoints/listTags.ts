const tagMapper = {
  BLUE: 'tag-blue',
  RED: 'tag-red',
  GREEN: 'tag-green',
  PURPLE: 'tag-purple',
};

export const listTagsDef = {
  endpoint: 'tags.list',
  mapResponse: response => response.data.map(t => ({id: t.id, name: t.name, color: tagMapper[t.color] || 'tag-blue'})),
};