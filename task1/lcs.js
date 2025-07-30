a = process.argv.slice(2);
if (!a.length) return '';
for(l=(m=a.reduce((a, b)=>a.length<=b.length?a:b)).length;l>0;l--) {for(i=0;i<=m.length-l;i++) {sb=m.slice(i,i+l);if(a.every(s=>s.includes(sb))) {return console.log(sb);}}}
