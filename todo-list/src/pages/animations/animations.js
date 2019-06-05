import React,{useState} from "react";
import { useSpring, animated, interpolate  } from 'react-spring'
import {ReactComponent as Icon} from '../../logo.svg'

import './style.css'

function Animations() {
  const props = useSpring({
    from: { left: '50%', top: '50%', width: '0%', height: '0%' },
    to: { left: '-50%', top: '-50%', width: '200%', height: '200%', borderRadius: '50%', background: 'lightblue' }
    ,config: { duration: 500 }
  })
  return <animated.div className="script-box" style={props} />
}

export default Animations;

