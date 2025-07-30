const [,...a]=process.argv;
for(let l=(m=a.reduce((a,b)=>a.length<=b.length?a:b)).length;l;l--)
  for(let i=0;i<=m.length-l;i++)
     if((s=m.slice(i,i+l))&&a.every(x=>x.includes(s)))
      console.log(s)||process.exit();
console.log('');